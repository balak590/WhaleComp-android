package com.klhk.whalecomp.utilities;

import android.util.Log;
import android.widget.Toast;

import com.klhk.whalecomp.Preference;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import wallet.core.jni.CoinType;
import wallet.core.jni.HDWallet;
import wallet.core.jni.PrivateKey;

import static com.klhk.whalecomp.utilities.Constants.BNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.LAST_EXECUTION_BLOCK;
import static com.klhk.whalecomp.utilities.Constants.WBNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;

public class FunctionCall {

    public static String pancakeRouterv2Factory ="0XCA143CE32FE78F1F7019D7D551A6402FC5350C73".toLowerCase();
    public static String pancakeRouterv2 = "0X10ED43C718714EB63D5AA57B78B54704E256024E".toLowerCase();
    public static String nullAddress ="0x0000000000000000000000000000000000000000000000000000000000000000";
    static Web3j web3 = Web3j.build(new HttpService("https://bsc-dataseed1.binance.org:443"));
    static HDWallet wallet = new HDWallet(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_PATTERN), "");
    static PrivateKey pk = wallet.getKeyForCoin(CoinType.SMARTCHAIN);
    static Credentials credentials = Credentials.create(ECKeyPair.create(pk.data()));
    public static BigInteger gasLimit = BigInteger.valueOf(500000);
    public static long gasPerTx = 120000;
    public static double pancakeTradingFee = 0.0025;
    public static ArrayList poolPath,fees;
    public static long gas =0;

    public static List<org.web3j.abi.datatypes.Address> tokenPath;
    public static boolean output = false;

    public static EthBlock getBlock(String txHash){

        try{
            Log.e("getBlock: ", txHash);
            EthTransaction ethTransaction =web3.ethGetTransactionByHash(txHash).sendAsync().get();
//            Log.e("block", String.valueOf(ethTransaction.getResult().getBlockNumber()));
            EthBlock ethBlock = web3.ethGetBlockByNumber(
                    DefaultBlockParameter.valueOf(ethTransaction.getResult().getBlockNumber()), true).sendAsync().get();
             //block = web3.ethGetBlockByNumber(DefaultBlockParameter.valueOf(ethTransaction.getResult().getBlockNumber()),true).sendAsync().get();

            Log.e("Blocl", String.valueOf(ethBlock.getBlock().getNumber()));
            return ethBlock;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static boolean checkTransactionByHash(String txHash){

        try{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        EthTransaction ethTransaction =web3.ethGetTransactionByHash(txHash).sendAsync().get();
                        if(ethTransaction.getTransaction().isPresent()){
//                            Log.e("tx", String.valueOf(ethTransaction.getResult().getBlockNumber()));
//                            Log.e("Present","true");
                            if(ethTransaction.getResult().getBlockNumber()==null){
                                Log.e("Present","false");
                                output= false;
                            }else{
                                Log.e("Present","true");
                                output= true;
                            }
                        }else{
                            Log.e("Present","false");
                            output= false;
                        }
//                        if(ethTransaction.hasError()){
//                            Log.e("eth blk err",ethTransaction.getError().getMessage());
//                            output= false;
//                        }else{
//
//                        }

                    }catch (Exception e){

                        e.printStackTrace();
                        output= false;
                    }

                }
            }).start();

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return output;
    }

    public static boolean getAllowance(String scAddress,String routerAddress){
        try{
            Function function = new Function(
                    "allowance",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(credentials.getAddress()),
                            new org.web3j.abi.datatypes.Address(routerAddress)),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);


            BigInteger nonce =  web3.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();
            Transaction transaction = Transaction.createFunctionCallTransaction(
                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
                    nonce,
                    gasPrice,
                    gasLimit,
                    scAddress,
                    BigInteger.ZERO,
                    encodedFunction);

            EthCall call = web3.ethCall(transaction,DefaultBlockParameterName.LATEST).sendAsync().get();
            if(call.hasError()){
                Log.e("allowence Error",call.getError().getMessage());
                return false;
            }else{
                Log.e("allowence",call.getValue());
                if(call.getValue().equalsIgnoreCase("0x0000000000000000000000000000000000000000000000000000000000000000")){
                    return false;
                }else{
                    return true;
                }
            }


        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static String approveTokens(String scAddress,String routerAddress){
        try{

            BigInteger transferAmount = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639935");

            Log.e("SCADDRESS",scAddress);

            Function function =  new Function(
                    "approve",
                    Arrays.asList(new Address(routerAddress), new Uint256(transferAmount)),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);
            Log.e("txData",encodedFunction);
            BigInteger nonce =  web3.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            //BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();
            BigInteger gasPrice =Convert.toWei("5", Convert.Unit.GWEI).toBigInteger();
            Log.e("GASPRICE", String.valueOf(gasPrice));
            RawTransaction rawTransaction =
                    RawTransaction.createTransaction(
                            nonce, gasPrice, gasLimit, scAddress, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            if(ethSendTransaction.hasError()){
                Log.e("transactionError",ethSendTransaction.getError().getMessage());
                return null;
            }else{
                String transactionHash = ethSendTransaction.getTransactionHash();

                Log.e("transactionHash",transactionHash);
                return transactionHash;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static String transferTokens(String receiverAddress, String scAddress, double amount){
        try{

            BigInteger transferAmount = Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger();

            Log.e("SCADDRESS",scAddress);

            Function function =  new Function(
                    "transfer",
                    Arrays.asList(new Address(receiverAddress), new Uint256(transferAmount)),
                    Collections.singletonList(new TypeReference<Bool>() {
                    }));
            String encodedFunction = FunctionEncoder.encode(function);
            BigInteger nonce =  web3.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();

            RawTransaction rawTransaction =
                    RawTransaction.createTransaction(
            nonce, gasPrice, gasLimit, scAddress, encodedFunction);

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


    public static String swapTokens(String token1sent, String token2sent, String amount, String slippage){
        try{
            Log.e("amount",amount);
            Log.e("slippage",slippage);
//            if(!token1sent.equalsIgnoreCase(BNB_ADDRESS)){
//                approveTokens(token1sent);
//            }
            List<Type> ouputList = getQuote(token1sent,token2sent,amount,slippage);
            Function function;

            if(token1sent.equalsIgnoreCase(BNB_ADDRESS)){
                function = new Function(
                        "swapExactETHForTokens",
                        ouputList,
                        Collections.singletonList(new TypeReference<Bool>() {
                        }));
            }else if(token2sent.equalsIgnoreCase(BNB_ADDRESS)){
                function = new Function(
                        "swapExactTokensForETH",
                        ouputList,
                        Collections.singletonList(new TypeReference<Bool>() {
                        }));
            }else{
                function = new Function(
                        "swapExactTokensForTokens",
                        ouputList,
                        Collections.singletonList(new TypeReference<Bool>() {
                        }));
            }


            String encodedFunction = FunctionEncoder.encode(function);
            BigInteger nonce =  web3.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();
            BigInteger value = Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger();
            RawTransaction rawTransaction;

            if(token1sent.equalsIgnoreCase(BNB_ADDRESS)){
                rawTransaction =
                        RawTransaction.createTransaction(
                                nonce, gasPrice, gasLimit, pancakeRouterv2,value, encodedFunction);
            }else{
                rawTransaction =
                        RawTransaction.createTransaction(
                                nonce, gasPrice, gasLimit, pancakeRouterv2, encodedFunction);
            }
            Log.e("Txdata",rawTransaction.getData());
            Log.e("Nonce", String.valueOf(rawTransaction.getNonce()));
            Log.e("Nonce", String.valueOf(rawTransaction.getGasPrice()));



            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            if(ethSendTransaction.hasError()){
                String transactionHash = ethSendTransaction.getError().getMessage();
                Log.e("transactionHash",transactionHash);
                return transactionHash;
            }else{
                String transactionHash = ethSendTransaction.getTransactionHash();
                Log.e("transactionHash",transactionHash);
                return transactionHash;
            }

//            return null;

//            Transaction transaction = Transaction.createFunctionCallTransaction(
//                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
//                    nonce,
//                    gasPrice,
//                    gasLimit,
//                    pancakeRouterv2,
//                    BigInteger.ZERO,
//                    encodedFunction);
//
//            EthCall call = web3.ethCall(transaction,DefaultBlockParameterName.LATEST).sendAsync().get();
//            if(call.hasError()){
//                Log.e("Error",call.getError().getMessage());
//                Log.e("Error",call.getRevertReason());
//                return null;
//            }else{
//                Log.e("call",call.getValue());
//                return call.getValue();
//            }
//            return null;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }


    public static List<Type> getQuote(String token1sent, String token2sent, String amount, String slippage){
        String token1= token1sent;
        String token2= token2sent;
        poolPath = new ArrayList();
        tokenPath = new ArrayList();
        fees = new ArrayList();
        double swapAmount=Double.parseDouble(amount);
        List<Type> ouputList = new ArrayList<Type>();

        if(!token1.equalsIgnoreCase(token2)){
            if(token1.equalsIgnoreCase(BNB_ADDRESS)){
                token1=WBNB_ADDRESS;
            }
            if(token2.equalsIgnoreCase(BNB_ADDRESS)){
                token2=WBNB_ADDRESS;
            }
            tokenPath.add(new org.web3j.abi.datatypes.Address(token1));
            if(!token1.equalsIgnoreCase(WBNB_ADDRESS) && !token2.equalsIgnoreCase(WBNB_ADDRESS)){
                ArrayList temp = getQuoteSinglePair(token1,WBNB_ADDRESS,swapAmount,slippage);
                gas += gasPerTx;
                swapAmount = (double) temp.get(0);
                poolPath.add(temp.get(1));
                fees.add(temp.get(2));
                tokenPath.add(new org.web3j.abi.datatypes.Address(WBNB_ADDRESS));
                token1 = WBNB_ADDRESS;
            }
            ArrayList temp1 = getQuoteSinglePair(token1,token2,swapAmount,slippage);
            Log.e("temp1",temp1.toString());
            gas += gasPerTx;
            swapAmount = (double) temp1.get(0);
            poolPath.add(temp1.get(1));
            fees.add(temp1.get(2));
            tokenPath.add(new org.web3j.abi.datatypes.Address(token2));
            //Convert.toWei(String.valueOf(swapAmount * (1-slippage)), Convert.Unit.ETHER).toBigInteger();
            if(!token1sent.equalsIgnoreCase(BNB_ADDRESS)){
                ouputList.add(new Uint256(Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger()));
            }
            Log.e("Conver", String.valueOf(Convert.toWei(String.valueOf(swapAmount * (1-Double.parseDouble(slippage))), Convert.Unit.ETHER).toBigInteger()));
            Log.e("uint", String.valueOf(new Uint256(Convert.toWei(String.valueOf(swapAmount * (1-Double.parseDouble(slippage))), Convert.Unit.ETHER).toBigInteger())));
            ouputList.add(new Uint256(Convert.toWei(String.valueOf(swapAmount * (1-Double.parseDouble(slippage))), Convert.Unit.ETHER).toBigInteger()));

            //ouputList.add(gas);
            //ouputList.add(new DynamicArray(poolPath));
            ouputList.add(new DynamicArray<>(Address.class,tokenPath));
            ouputList.add(new org.web3j.abi.datatypes.Address(credentials.getAddress()));
            Date currentDate = new Date();
            long milliSeconds = currentDate.getTime();
            ouputList.add(new Uint256(BigInteger.valueOf((long) ((milliSeconds/1000)+60*10))));

            Log.e("Output",ouputList.toString());
            return ouputList;
        }
        else{
            return null;
        }

    }

    public static ArrayList getQuoteSinglePair(String token1, String token2, double amount, String slippage){
        String pairPool = "";
        String reserveDetail = "";
        String token0 = "";
        pairPool = getPair(token1,token2);
        ArrayList<Object> ouputList = new ArrayList<>();
        double reserve0,reserve1,coeff,reserve0New,reserve1New,slip;
        if(pairPool.equalsIgnoreCase(nullAddress)){

        }else{
            pairPool = "0x"+pairPool.substring(pairPool.length()-40);
            Log.e("pairPool",pairPool);
            reserveDetail=getReserves(pairPool);
            String toke1 = reserveDetail.substring(2,66);
            String toke2 = reserveDetail.substring(66,66+64);
            Log.e("toke1",toke1);
            Log.e("toke2",toke2);
            reserve0 = Double.parseDouble(Hex.hexToDecimal("0x"+toke1))/Math.pow(10,16);
            reserve1 = Double.parseDouble(Hex.hexToDecimal("0x"+toke2))/Math.pow(10,16);
            Log.e("reserve0", String.valueOf(reserve0));
            Log.e("reserve1", String.valueOf(reserve1));
            token0 = getToken0(pairPool);
            token0 = "0x"+token0.substring(token0.length()-40);
            coeff = reserve0 * reserve1;

            if(token1.equalsIgnoreCase(token0)){
                reserve0New = reserve0 + amount;
                reserve1New = coeff / reserve0New;
                slip = 1- reserve1New/reserve0New/(reserve1/reserve0);
                if (slip > Double.parseDouble(slippage)) {
                    return null;
                } else{
                    ouputList.add((reserve1 - reserve1New) * (1- pancakeTradingFee));
                    ouputList.add(pairPool);
                    ouputList.add((reserve1 - reserve1New) * pancakeTradingFee);
                }
            }
            else{
                reserve1New = reserve1 + amount;
                reserve0New = coeff / reserve1New;
                slip = 1- reserve0New/reserve1New/(reserve0/reserve1);
                if (slip > Double.parseDouble(slippage)) {
                    return null;
                } else{
                    ouputList.add((reserve0 - reserve0New) * (1- pancakeTradingFee));
                    ouputList.add(pairPool);
                    ouputList.add((reserve0 - reserve0New) * pancakeTradingFee);
                }
            }

        }
        return ouputList;

    }

    public static String getDecimals(String SCAddress){
        try{

                Function function = new Function(
                        "decimals",
                        Arrays.<Type>asList(),
                        Collections.emptyList());
                String encodedFunction = FunctionEncoder.encode(function);

                org.web3j.protocol.core.methods.response.EthCall response = web3.ethCall(
                        Transaction.createEthCallTransaction("0xeaEE42048b0A8eB16E59Ca104B733E8e483012e6", SCAddress, encodedFunction),
                        DefaultBlockParameterName.LATEST)
                        .sendAsync().get();

                if(response.hasError()){
                    Log.e("Error3",response.getError().getMessage());
                    return null;
                }else{
                    Log.e("call",response.getValue());
                    return response.getValue();
                }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String getBalance(String walletAddress, String SCAddress){
        try{
            //int decimal = Integer.parseInt(getDecimals(SCAddress));
            if(SCAddress.equalsIgnoreCase(BNB_ADDRESS)){
                EthGetBalance output = web3.ethGetBalance(walletAddress,DefaultBlockParameterName.LATEST).sendAsync().get();
                BigInteger bal = output.getBalance();
                return divideBy18(bal);
            }else{
                Function function = new Function(
                        "balanceOf",
                        Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(walletAddress)),
                        Collections.emptyList());
                String encodedFunction = FunctionEncoder.encode(function);

                org.web3j.protocol.core.methods.response.EthCall response = web3.ethCall(
                        Transaction.createEthCallTransaction(walletAddress, SCAddress, encodedFunction),
                        DefaultBlockParameterName.LATEST)
                        .sendAsync().get();

                if(response.hasError()){
                    Log.e("Error3",response.getError().getMessage());
                    return null;
                }else{
                    Log.e("call",Hex.hexToBigInteger(response.getValue()).toString());
                    return divideBy18(Hex.hexToBigInteger(response.getValue()));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String getBalanceBigInt(String walletAddress, String SCAddress){
        try{
            //int decimal = Integer.parseInt(getDecimals(SCAddress));
            if(SCAddress.equalsIgnoreCase(BNB_ADDRESS)){
                EthGetBalance output = web3.ethGetBalance(walletAddress,DefaultBlockParameterName.LATEST).sendAsync().get();
                BigInteger bal = output.getBalance();
                return divideBy18(bal);
            }else{
                Function function = new Function(
                        "balanceOf",
                        Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(walletAddress)),
                        Collections.emptyList());
                String encodedFunction = FunctionEncoder.encode(function);

                org.web3j.protocol.core.methods.response.EthCall response = web3.ethCall(
                        Transaction.createEthCallTransaction(walletAddress, SCAddress, encodedFunction),
                        DefaultBlockParameterName.LATEST)
                        .sendAsync().get();

                if(response.hasError()){
                    Log.e("Error3",response.getError().getMessage());
                    return null;
                }else{
                    Log.e("call",Hex.hexToBigInteger(response.getValue()).toString());
                    return String.valueOf(Hex.hexToBigInteger(response.getValue()));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

//    public static String getTxList(String scAddress){
//        web3.tr
//    }

    public static String divideBy18(BigInteger amount){

        return String.valueOf(Convert.fromWei(amount.toString(), Convert.Unit.ETHER));
    }

    public static String multipliedBy18(String amount){

        return String.valueOf(Convert.toWei(amount, Convert.Unit.ETHER));
    }

    public static String getPair(String token1, String token2){
        try{
            Function function = new Function(
                    "getPair",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(token1),
                            new org.web3j.abi.datatypes.Address(token2)),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);

//            Transaction transaction = Transaction.createFunctionCallTransaction(
//                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
//                    nonce,
//                    gasPrice,
//                    gasLimit,
//                    pancakeRouterv2Factory,
//                    BigInteger.ZERO,
//                    encodedFunction);
////            Transaction transaction = Transaction.createFunctionCallTransaction(
////                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
////                    nonce,
////                    BigInteger.ZERO,
////                    BigInteger.ZERO,
////                    pancakeRouterv2Factory,
////                    BigInteger.ZERO,
////                    encodedFunction)
////               ;


            org.web3j.protocol.core.methods.response.EthCall response = web3.ethCall(
                    Transaction.createEthCallTransaction(wallet.getAddressForCoin(CoinType.SMARTCHAIN), pancakeRouterv2Factory, encodedFunction),
            DefaultBlockParameterName.LATEST)
             .sendAsync().get();

//            List<Type> someTypes = FunctionReturnDecoder.decode(
//                    response.getValue(), function.getOutputParameters());



//            EthCall call = web3.ethCall(transaction,DefaultBlockParameterName.LATEST).sendAsync().get();
//            EthCall call = web3.ethCall(transaction,DefaultBlockParameterName.LATEST).send();

            if(response.hasError()){
                Log.e("Error3",response.getError().getMessage());
                return null;
            }else{
                Log.e("call",response.getValue());
                return response.getValue();
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String getReserves(String poolPair){
        try{
            Function function = new Function(
                    "getReserves",
                    Arrays.<Type>asList(),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);
//            BigInteger nonce =  web3.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
//            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();
//            Transaction transaction = Transaction.createFunctionCallTransaction(
//                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
//                    nonce,
//                    gasPrice,
//                    gasLimit,
//                    poolPair,
//                    BigInteger.ZERO,
//                    encodedFunction);
//
//            EthCall call = web3.ethCall(transaction,DefaultBlockParameterName.LATEST).sendAsync().get();
            org.web3j.protocol.core.methods.response.EthCall call = web3.ethCall(
                    Transaction.createEthCallTransaction(wallet.getAddressForCoin(CoinType.SMARTCHAIN), poolPair, encodedFunction),
                    DefaultBlockParameterName.LATEST)
                    .sendAsync().get();
            if(call.hasError()){
                Log.e("Error 1",call.getError().getMessage());
                return null;
            }else{
                Log.e("call",call.getValue());
                return call.getValue();
            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    public static String getToken0(String poolPair){
        try{
            Function function = new Function(
                    "token0",
                    Arrays.<Type>asList(),
                    Collections.emptyList());
            String encodedFunction = FunctionEncoder.encode(function);
//            BigInteger nonce =  web3.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
//            BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();
//            Transaction transaction = Transaction.createFunctionCallTransaction(
//                    wallet.getAddressForCoin(CoinType.SMARTCHAIN),
//                    nonce,
//                    gasPrice,
//                    gasLimit,
//                    poolPair,
//                    BigInteger.ZERO,
//                    encodedFunction);

//            EthCall call = web3.ethCall(transaction,DefaultBlockParameterName.LATEST).sendAsync().get();
            org.web3j.protocol.core.methods.response.EthCall call = web3.ethCall(
                    Transaction.createEthCallTransaction(wallet.getAddressForCoin(CoinType.SMARTCHAIN), poolPair, encodedFunction),
                    DefaultBlockParameterName.LATEST)
                    .sendAsync().get();
            if(call.hasError()){
                Log.e("Error 2",call.getError().getMessage());
                return null;
            }else{
                Log.e("call",call.getValue());
                return call.getValue();
            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }


    public static double getSwapAmount(String token1sent, String token2sent, String amount, String slippage){
        try{
            String token1= token1sent;
            String token2= token2sent;
            poolPath = new ArrayList();
            tokenPath = new ArrayList();
            fees = new ArrayList();
            double swapAmount=Double.parseDouble(amount);
            List<Type> ouputList = new ArrayList<Type>();

            if(!token1.equalsIgnoreCase(token2)){
                if(token1.equalsIgnoreCase(BNB_ADDRESS)){
                    token1=WBNB_ADDRESS;
                }
                if(token2.equalsIgnoreCase(BNB_ADDRESS)){
                    token2=WBNB_ADDRESS;
                }
                tokenPath.add(new org.web3j.abi.datatypes.Address(token1));
                if(!token1.equalsIgnoreCase(WBNB_ADDRESS) && !token2.equalsIgnoreCase(WBNB_ADDRESS)){
                    ArrayList temp = getQuoteSinglePair(token1,WBNB_ADDRESS,swapAmount,slippage);
                    gas += gasPerTx;
                    swapAmount = (double) temp.get(0);
                    poolPath.add(temp.get(1));
                    fees.add(temp.get(2));
                    tokenPath.add(new org.web3j.abi.datatypes.Address(WBNB_ADDRESS));
                    token1 = WBNB_ADDRESS;
                }
                ArrayList temp1 = getQuoteSinglePair(token1,token2,swapAmount,slippage);
                Log.e("temp1",temp1.toString());
                gas += gasPerTx;
                swapAmount = (double) temp1.get(0);
                return swapAmount * (1-Double.parseDouble(slippage));
            }
            else{
                return 0;
            }
        }catch (Exception e){

            e.printStackTrace();
            return 0;
        }
    }



}
