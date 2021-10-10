package com.klhk.whalecomp.utilities;

import android.os.Build;
import android.util.Log;
import android.view.View;

import com.klhk.whalecomp.Preference;

import org.json.JSONArray;
import org.json.JSONObject;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import wallet.core.jni.CoinType;
import wallet.core.jni.HDWallet;
import wallet.core.jni.PrivateKey;

import static com.klhk.whalecomp.utilities.Constants.APY_CALCULATED_EPS;
import static com.klhk.whalecomp.utilities.Constants.APY_CALCULATED_EPS_NON_COMPOUND;
import static com.klhk.whalecomp.utilities.Constants.BNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.CAN_EXECUTE_COMPOUNDING;
import static com.klhk.whalecomp.utilities.Constants.EPSSTAKED;
import static com.klhk.whalecomp.utilities.Constants.EPS_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.LAST_EXECUTION_BLOCK;
import static com.klhk.whalecomp.utilities.Constants.LAST_EXECUTION_TIME;
import static com.klhk.whalecomp.utilities.Constants.NEXT_EXECUTION_TIME;
import static com.klhk.whalecomp.utilities.Constants.STAKED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.diffInDays;
import static com.klhk.whalecomp.utilities.Constants.diffTimeSecond;
import static com.klhk.whalecomp.utilities.FunctionCall.divideBy18;
import static com.klhk.whalecomp.utilities.FunctionCall.pancakeRouterv2;
import static com.klhk.whalecomp.utilities.Hex.hexToBigInteger;
import static com.klhk.whalecomp.utilities.Hex.hexToDecimal;

public class Strategy001 {

    static Web3j web3 = Web3j.build(new HttpService("https://bsc-dataseed1.binance.org:443"));
    static HDWallet wallet = new HDWallet(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_PATTERN), "");
    static PrivateKey pk = wallet.getKeyForCoin(CoinType.SMARTCHAIN);
    static Credentials credentials = Credentials.create(ECKeyPair.create(pk.data()));
    public static BigInteger gasLimit = BigInteger.valueOf(500000);
    public static long gasPerTx = 120000;
    public static String epsSCAddress = "0xa7f552078dcc247c2684336020c03648500c6d9f".toLowerCase();
    public static String epsStakerAddress = "0x4076CC26EFeE47825917D0feC3A79d0bB9a6bB5c".toLowerCase();
    public static Date lastExec;
    public static EthBlock block;

    public static void ExecuteStrategy(){
        int txsComp = 2;
        boolean reinvWithEntireBal = true;
        int reinvestPerc = 1;
        double step=0.001;

        JSONArray strArray = new JSONArray();



        try{
            if(!Preference.getInstance().returnValue(STAKED_ADDRESS).isEmpty()) {
                strArray = new JSONArray(Preference.getInstance().returnValue(STAKED_ADDRESS));

                for(int i=0;i<strArray.length();i++){
                    JSONObject object1 = new JSONObject(strArray.get(i).toString());
                    Log.e("object1",object1.toString());
                    boolean canCoumpound = false;
                    if(object1.get(NEXT_EXECUTION_TIME)!=null&&object1.get(LAST_EXECUTION_TIME)!=null){
                        Date nextExecTime = new Date(Long.parseLong(object1.get(NEXT_EXECUTION_TIME).toString()));
                        Date date2 = new Date(Long.parseLong(object1.get(LAST_EXECUTION_TIME).toString())*1000);
                        long millis = (long)(nextExecTime.getTime() - date2.getTime())/4;

                        if(System.currentTimeMillis() > date2.getTime()+millis){
                            canCoumpound= true;
                        }else{
                            canCoumpound= false;
                        }
                    }else{
                        canCoumpound= true;
                    }

                    if (object1.get(WHALETRUST_ADDRESS_SELECTED).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))){

                        //Log.e("LAST_EXECUTION_TIME",Preference.getInstance().returnValue(LAST_EXECUTION_TIME));
                        EthBlock ethBlock = web3.ethGetBlockByNumber(
                                DefaultBlockParameter.valueOf(new BigInteger(object1.getString(LAST_EXECUTION_BLOCK))), true).sendAsync().get();

                        //block = getLogs();
                        //BigInteger lastExecuteTime = new Bobject1.getString(LAST_EXECUTION_BLOCK);
                        //Preference.getInstance().writePreference(LAST_EXECUTION_BLOCK, String.valueOf(ethBlock.getBlock().getNumber()));

                        Log.e("block time",String.valueOf(ethBlock.getBlock().getTimestamp()));


                        //bnb bal
                        String bnbBal = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), BNB_ADDRESS);
                        Log.e("bnbBal",bnbBal);
                        //eps bal
                        String epsBal = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), EPS_ADDRESS);
                        Log.e("epsBal",epsBal);
                        String claimBalance = claimableRewards(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
                        Log.e("claimBalance",claimBalance);
                        String[] claimBal = claimBalance.split("000000000000000000000000a7f552078dcc247c2684336020c03648500c6d9f");

                        String epsClaim = hexToDecimal("0x"+claimBal[1].substring(0,64));
                        Log.e("claim Bal1",claimBal[1].substring(0,64));
                        claimBal = claimBalance.split("000000000000000000000000e9e7cea3dedca5984780bafc599bd69add087d56");
                        Log.e("claim Bal2",claimBal[1].substring(0,64));
                        String busdClaim = hexToDecimal("0x"+claimBal[1].substring(0,64));

                        Log.e("epsClaim",epsClaim);
                        Log.e("busdClaim",busdClaim);



                        double bnbPrice = FunctionCall.getSwapAmount(BNB_ADDRESS,BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));
                        double epsPrice = FunctionCall.getSwapAmount(EPS_ADDRESS,BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));



                        String totalBalStakeLock = FunctionCall.divideBy18(hexToBigInteger(totalBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS))));
                        Log.e("totalBalStakeLock",totalBalStakeLock);


                        String lockedBalance = lockedBalances(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));


                        int epocheInd = 2+64*4;
                        int nEpoche = Hex.hexToInteger(("0x"+lockedBalance.substring(258,322)));
                        Log.e("nEpoche", String.valueOf(nEpoche));
                        epocheInd+=64;
                        for(int j=0;j<nEpoche;j++){
                            String epochR = FunctionCall.divideBy18(hexToBigInteger("0x"+lockedBalance.substring(epocheInd,epocheInd+64)));
                            Log.e("Epoch","1 "+epochR );
                            epocheInd += 64*2;
                        }

                        double gasCostVal;
                        try{
                            BigInteger getRewardGas = getGasEstimate("getReward",new ArrayList<Type>());
                            Log.e("getRewardGas", String.valueOf(getRewardGas));
                            List<Type> ouputList = new ArrayList<Type>();
                            ouputList.add(new Uint256(BigInteger.TEN));
                            ouputList.add(new Bool(true));
                            BigInteger stakeGas =getGasEstimate("stake",ouputList);
                            Log.e("stakeGas", String.valueOf(stakeGas));
                            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();

                            BigInteger gasCost = (getRewardGas.add(stakeGas)).multiply(gasPrice);

                            gasCostVal = Double.parseDouble(String.valueOf(divideBy18(gasCost)));



                        }catch (Exception e){
                            gasCostVal= (136592 + 150620) * 0.000000005;
                            e.printStackTrace();
                        }

                        double gasCostUsd = gasCostVal * bnbPrice;

                        Log.e("gasCostUsd",String.valueOf(gasCostUsd));

                        Date evalTime = new Date();
                        //long evalTime = date.getTime();

                        lastExec = new Date(Long.parseLong(String.valueOf(ethBlock.getBlock().getTimestamp()))*1000);

                        Log.e("lastExec", String.valueOf(lastExec.getTime()));
                        Log.e("evalTime", String.valueOf(evalTime.getTime()));



//            double diffTimeDays = diffInDays(lastExec,evalTime);
//            double diffTimeDays =(evalTime.getTime()-lastExec.getTime())/(1000*60*60*24);
                        double diffTimeDays =((float)evalTime.getTime()-(float) lastExec.getTime())/(1000*60*60*24);
                        Log.e("diffTimeDays", String.format("%.15f", diffTimeDays));

                        EthBlock latestEthBlock = web3.ethGetBlockByNumber(
                                DefaultBlockParameterName.LATEST, false).sendAsync().get();
                        BigInteger currentBlock = latestEthBlock.getBlock().getNumber();
                        //EthBlock lastExecuteBlock = web3.ethGetBlockByNumber(DefaultBlockParameter.valueOf(object1.getString(LAST_EXECUTION_BLOCK)),true).sendAsync().get();
                        Log.e("Current block", String.valueOf(latestEthBlock.getBlock().getNumber()));
                        Log.e("Old Block",String.valueOf(object1.getString(LAST_EXECUTION_BLOCK)));

                        int maxComps = (int)((currentBlock.subtract(new BigInteger(object1.getString(LAST_EXECUTION_BLOCK)))).intValue())/txsComp;

                        double optimalDailyReturn = -1;
                        double optimalDailyComps = 0.0,optimalEpochComps=0.0;
                        double dailyComps1=0.0;
                        double nonCompReturn=(Double.parseDouble(String.valueOf(Convert.fromWei(epsClaim, Convert.Unit.ETHER)))+
                                Double.parseDouble(String.valueOf(Convert.fromWei(busdClaim, Convert.Unit.ETHER)))/ epsPrice)
                                /Double.parseDouble(totalBalStakeLock)
                                *(365/diffTimeDays)*100;

                        for (double c = 1; c > 0 ; c -= step) {
                            Log.e("J",String.valueOf(c));
                            double epsPerComp = (Double.parseDouble(String.valueOf(Convert.fromWei(epsClaim, Convert.Unit.ETHER))) + Double.parseDouble(String.valueOf(Convert.fromWei(busdClaim, Convert.Unit.ETHER))) / epsPrice) / c;  // Include both reward tokens
//                            Log.e("epsPerComp1", String.valueOf(epsPerComp));
                            double epsNetPerComp = epsPerComp - gasCostUsd / epsPrice;
//                double epsNetPerComp = epsPerComp;
//                            Log.e("epsPerComp", String.valueOf(epsPerComp));
//                            Log.e("epsPrice", String.valueOf(epsPrice));
//                            Log.e("epsNetPerComp1", String.valueOf(epsNetPerComp));
                             dailyComps1 = c / diffTimeDays;
//                            Log.e("diffTimeDays", String.valueOf(diffTimeDays));
                            double epsNetPerCompDailyReturn = Math.pow((epsNetPerComp / Double.parseDouble(totalBalStakeLock)) + 1 , dailyComps1) - 1 ;// EPS Daily % Return of this compound period

                            if (epsNetPerCompDailyReturn > optimalDailyReturn) { // Better return found
                                optimalDailyReturn = epsNetPerCompDailyReturn;

                                optimalDailyComps = dailyComps1;

                                optimalEpochComps = c;
                            } else { // Return downtrend
//                                Log.e("epsNetPerCompDailyReturn",String.valueOf(epsNetPerCompDailyReturn));
                                break;
                            }
                        }
                        Log.e("optimalDailyReturn", String.valueOf(optimalDailyReturn));
                        Log.e("optimalDailyComps", String.valueOf(optimalDailyComps));
                        Log.e("dailyComps1", String.valueOf(dailyComps1));
                        double dailyComps=0.0;
                        for (double c=1; c<=maxComps; c+= step) {
                            Log.e("epsClaim2",String.valueOf(Convert.fromWei(epsClaim, Convert.Unit.ETHER)));
//                BigDecimal addupReward = Convert.fromWei(epsClaim, Convert.Unit.ETHER).add(Convert.fromWei(busdClaim, Convert.Unit.ETHER));
                            Log.e("C", String.valueOf(c));
                            double epsPerComp = (Double.parseDouble(String.valueOf(Convert.fromWei(epsClaim, Convert.Unit.ETHER))) + Double.parseDouble(String.valueOf(Convert.fromWei(busdClaim, Convert.Unit.ETHER)))/ epsPrice) / c;  // Include both reward tokens
                            Log.e("epsPerComp2", String.valueOf(epsPerComp));
                            double epsNetPerComp = (epsPerComp - gasCostUsd )/ epsPrice;
//                double epsNetPerComp = epsPerComp;
                            Log.e("epsNetPerComp2", String.valueOf(epsNetPerComp));
                            dailyComps = (double)c / diffTimeDays;
                            double epsNetPerCompDailyReturn = Math.pow(epsNetPerComp / Double.parseDouble(totalBalStakeLock) + 1 , dailyComps) - 1 ;  // EPS Daily % Return of this compound period
                            if (epsNetPerCompDailyReturn > optimalDailyReturn) { // Better return found
                                optimalDailyReturn = epsNetPerCompDailyReturn;
                                Log.e("optimalDailyReturn2", String.valueOf(optimalDailyReturn));
                                optimalDailyComps = dailyComps;
                                Log.e("optimalDailyComps2",String.valueOf(optimalDailyComps));

                                optimalEpochComps = c;
                                // console.log("Daily return: " + optimalDailyReturn)
                                // console.log("Daily comps: " +  optimalDailyComps)
                            } else { // Return downtrend
                                break;
                            }
                        }

                        long diffTimeSecond= (long) ((diffTimeSecond(lastExec,evalTime)*1000)/optimalEpochComps);

                        Log.e("optimal yearly comp",String.valueOf(optimalDailyComps*365));
                        Log.e("optimal APY",String.valueOf((Math.pow(optimalDailyReturn + 1, 365)-1)*100));
                        Log.e("nonCompReturn",String.valueOf(nonCompReturn));
                        String APY_CALCULATED_NON_COMPOUND = String.valueOf(nonCompReturn);

                        String APY_CALCULATED = String.valueOf((Math.pow(optimalDailyReturn + 1, 365)-1)*100);



                        Log.e("ENDED","#######################################################################");

                        if (optimalEpochComps >= 1){
                            //nextExecuteTime = lastExecuteTime + perCompTime;
                            Preference.getInstance().writeBooleanPreference(CAN_EXECUTE_COMPOUNDING,true);
                            try {
                                JSONArray array = new JSONArray();
                                if(!Preference.getInstance().returnValue(STAKED_ADDRESS).isEmpty()) {
                                    array = new JSONArray(Preference.getInstance().returnValue(STAKED_ADDRESS));
                                    JSONArray tempArray = new JSONArray();
                                    for (int j = 0; j < array.length(); j++) {
                                        JSONObject object = new JSONObject(array.get(i).toString());
                                        if (object.get(WHALETRUST_ADDRESS_SELECTED).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))) {
                                            if (object.get(EPSSTAKED).toString().equalsIgnoreCase("yes")) {
                                                object.put(APY_CALCULATED_EPS,APY_CALCULATED);
                                                object.put(CAN_EXECUTE_COMPOUNDING,true);
                                                object.put(APY_CALCULATED_EPS_NON_COMPOUND,APY_CALCULATED_NON_COMPOUND);
                                            }
                                        }

                                        tempArray.put(object);
                                    }
                                    Preference.getInstance().writePreference(STAKED_ADDRESS,tempArray.toString());
                                }


                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            long nextExecuteTime = lastExec.getTime() + (diffTimeSecond);

                            Log.e("diffTimeSecond", String.valueOf(diffTimeSecond));
                            Log.e("nextExecuteTime", String.valueOf(nextExecuteTime));

                            Date nextExc= new Date(nextExecuteTime);
                            Log.e("Last execution",String.valueOf(lastExec.getTime()));

                            Log.e("Next excution", String.valueOf(nextExc.getTime()));

                            try {
                                JSONArray array = new JSONArray();
                                if(!Preference.getInstance().returnValue(STAKED_ADDRESS).isEmpty()) {
                                    array = new JSONArray(Preference.getInstance().returnValue(STAKED_ADDRESS));
                                    JSONArray tempArray = new JSONArray();
                                    for (int j = 0; j < array.length(); j++) {
                                        JSONObject object = new JSONObject(array.get(i).toString());
                                        if (object.get(WHALETRUST_ADDRESS_SELECTED).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))) {
                                            if (object.get(EPSSTAKED).toString().equalsIgnoreCase("yes")) {
                                                object.put(APY_CALCULATED_EPS,APY_CALCULATED);
                                                object.put(APY_CALCULATED_EPS_NON_COMPOUND,APY_CALCULATED_NON_COMPOUND);
                                                object.put(NEXT_EXECUTION_TIME,String.valueOf(nextExc.getTime()));
                                                object.put(LAST_EXECUTION_TIME,String.valueOf((int)(lastExec.getTime()/1000)));
                                                object.put(CAN_EXECUTE_COMPOUNDING,false);
//                                    object.put(LAST_EXECUTION_BLOCK)

                                            }
                                        }

                                        tempArray.put(object);
                                    }
                                    Preference.getInstance().writePreference(STAKED_ADDRESS,tempArray.toString());
                                }


                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                }

            }





        }catch(Exception e){
            e.printStackTrace();

        }

    }

    public static BigInteger getGasEstimate(String method, List<Type> ouputList){
        try{
            Function function = new Function(
                    method,
                    ouputList,
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);

            Transaction transaction = Transaction.createEthCallTransaction(
                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
                    epsStakerAddress,
                    encodedFunction);

            EthEstimateGas response = web3.ethEstimateGas(transaction).sendAsync().get();

            if(response.hasError()){
                Log.e("Error",response.getError().getMessage());
                return null;
            }else{
                return response.getAmountUsed();
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String addresTo64Hex(String addr){
        String addrress = "0x000000000000000000000000"+addr.substring(2,addr.length());
        Log.e("addess",addrress);
        return addrress;
    }

    public static  EthBlock getLogs(){
        try{
            EthBlock latestEthBlock = web3.ethGetBlockByNumber(
                    DefaultBlockParameterName.LATEST, false).sendAsync().get();
            Log.e("Current", String.valueOf(latestEthBlock.getBlock().getNumber()));
            Log.e("old", String.valueOf(latestEthBlock.getBlock().getNumber().subtract(BigInteger.valueOf(2000))));

            DefaultBlockParameter block = DefaultBlockParameter.valueOf(latestEthBlock.getBlock().getNumber().subtract(BigInteger.valueOf(4000)));
            EthFilter filter = new EthFilter(block,DefaultBlockParameterName.LATEST,epsStakerAddress);
            filter.addSingleTopic("0x9e71bc8eea02a63969f509818f2dafb9254532904319f9dbda79b67bd34a5f3d");
            filter.addOptionalTopics(addresTo64Hex(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS)));
            //filter.addOptionalTopics("0x0000000000000000000000003d75c58e07ff3a44e296b0701b8a54055dbaa07e");
            EthLog log = web3.ethGetLogs(filter).sendAsync().get();
            if(log.hasError()){
                Log.e("error",log.getError().getMessage() );
                return null;
            }else{
                Log.e("result",log.getLogs().toString());
                if(log.getLogs().size()>0){
                    Log.e("Results", String.valueOf(log.getResult()));
                    String result = String.valueOf(log.getResult().get(log.getResult().size()-1));
                    JSONObject obj = new JSONObject(result.substring(3,result.length()));

                    EthBlock ethBlock = web3.ethGetBlockByNumber(
                            DefaultBlockParameter.valueOf(hexToBigInteger(obj.getString("blockNumber"))), true).sendAsync().get();
                    //ethBlock.getBlock().getTimestamp();
                    //Log.e("time", String.valueOf(ethBlock.getBlock().getTimestamp()));
                    return ethBlock;
                }else{
                    return null;
                }

            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    public static String claimableRewards(String walletAddress){
        try{
            Function function = new Function(
                    "claimableRewards",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(walletAddress)),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);


            BigInteger nonce =  web3.ethGetTransactionCount(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();
            Transaction transaction = Transaction.createEthCallTransaction(
                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
                    epsStakerAddress,
                    encodedFunction);

            EthCall call = web3.ethCall(transaction,DefaultBlockParameterName.LATEST).sendAsync().get();
            if(call.hasError()){
                Log.e("allowence Error",call.getError().getMessage());
                return call.getError().getMessage();
            }else{
                Log.e("allowence",call.getValue());
                return call.getValue();
            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String totalBalance(String walletAddress){
        try{
            Function function = new Function(
                    "totalBalance",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(walletAddress)),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);


            BigInteger nonce =  web3.ethGetTransactionCount(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();
            Transaction transaction = Transaction.createEthCallTransaction(
                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
                    epsStakerAddress,
                    encodedFunction);

            EthCall call = web3.ethCall(transaction,DefaultBlockParameterName.LATEST).sendAsync().get();
            if(call.hasError()){
                Log.e("allowence Error",call.getError().getMessage());
                return call.getError().getMessage();
            }else{
                Log.e("allowence",call.getValue());
                return call.getValue();
            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String lockedBalances(String walletAddress){
        try{
            Function function = new Function(
                    "lockedBalances",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(walletAddress)),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);

            Transaction transaction = Transaction.createEthCallTransaction(
                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
                    epsStakerAddress,
                    encodedFunction);

            EthCall call = web3.ethCall(transaction,DefaultBlockParameterName.LATEST).sendAsync().get();
            if(call.hasError()){
                Log.e("allowence Error",call.getError().getMessage());
                return call.getError().getMessage();
            }else{
                Log.e("allowence",call.getValue());
                return call.getValue();
            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String unLockedBalance(String walletAddress){
        try{
            Function function = new Function(
                    "unlockedBalance",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(walletAddress)),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);

            Transaction transaction = Transaction.createEthCallTransaction(
                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
                    epsStakerAddress,
                    encodedFunction);

            EthCall call = web3.ethCall(transaction,DefaultBlockParameterName.LATEST).sendAsync().get();
            if(call.hasError()){
                Log.e("allowence Error",call.getError().getMessage());
                return call.getError().getMessage();
            }else{
                Log.e("allowence",call.getValue());
                return call.getValue();
            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String withdraw(String amount){
        try{
            Function function = new Function(
                    "withdraw",
                    Arrays.<Type>asList(new Uint256(new BigInteger(amount))),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);


            BigInteger nonce =  web3.ethGetTransactionCount(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();

            RawTransaction rawTransaction =
                    RawTransaction.createTransaction(
                            nonce, gasPrice, gasLimit, epsStakerAddress, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();

            return transactionHash;


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String exit(){
        try{
            Function function = new Function(
                    "exit",
                    Arrays.<Type>asList(),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);


            BigInteger nonce =  web3.ethGetTransactionCount(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();

            RawTransaction rawTransaction =
                    RawTransaction.createTransaction(
                            nonce, gasPrice, gasLimit, epsStakerAddress, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();

            return transactionHash;


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String getReward(){
        try{
            Function function = new Function(
                    "getReward",
                    Arrays.<Type>asList(),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);


            BigInteger nonce =  web3.ethGetTransactionCount(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();

            RawTransaction rawTransaction =
                    RawTransaction.createTransaction(
                            nonce, gasPrice, gasLimit, epsStakerAddress, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();

            return transactionHash;


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String stake(){
        try{
            String amount = FunctionCall.getBalanceBigInt(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), EPS_ADDRESS);
            Function function = new Function(
                    "stake",
                    Arrays.<Type>asList(new Uint256(new BigInteger(amount)), new Bool(true)),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);


            BigInteger nonce =  web3.ethGetTransactionCount(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();

            RawTransaction rawTransaction =
                    RawTransaction.createTransaction(
                            nonce, gasPrice, gasLimit, epsStakerAddress, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();

            return transactionHash;


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    public static String stake(String amount){
        try{

//            Log.e("amount",String.valueOf(new BigInteger(amount)));

            Function function = new Function(
                    "stake",
                    Arrays.<Type>asList(new Uint256(new BigInteger(FunctionCall.multipliedBy18(amount))), new Bool(true)),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);


            BigInteger nonce =  web3.ethGetTransactionCount(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();

            RawTransaction rawTransaction =
                    RawTransaction.createTransaction(
                            nonce, gasPrice, gasLimit, epsStakerAddress, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();


            if(ethSendTransaction.hasError()){
                Log.e("stake error",ethSendTransaction.getError().getMessage());
                return ethSendTransaction.getError().getMessage();
            }else{
                String transactionHash = ethSendTransaction.getTransactionHash();
                return transactionHash;
            }




        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


}
