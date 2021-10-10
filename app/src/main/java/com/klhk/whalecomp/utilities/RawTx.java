package com.klhk.whalecomp.utilities;

import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;

public class RawTx {
    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private String to;
    private BigInteger value;
    private String data;
    private BigInteger gasPremium;
    private BigInteger feeCap;
    private int chainId;

    public RawTx(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data, int chainId) {
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.to = to;
        this.value = value;
        this.data = data;
        this.chainId = chainId;
    }

    public RawTx(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data, BigInteger gasPremium, BigInteger feeCap, int chainId) {
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.to = to;
        this.value = value;
        this.data = data;
        this.gasPremium = gasPremium;
        this.feeCap = feeCap;
        this.chainId = chainId;
    }

    public static RawTx createTransaction(
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            BigInteger value,
            String data,
            int chainId) {

        return new RawTx(nonce, gasPrice, gasLimit, to, value, data, chainId);
    }
    public static RawTx createTransaction(
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            BigInteger value,
            int chainId) {

        return new RawTx(nonce, gasPrice, gasLimit, to, value, "", chainId);
    }
    public boolean isLegacyTransaction() {
        return gasPrice != null && gasPremium == null && feeCap == null;
    }

    public boolean isEIP1559Transaction() {
        return gasPrice == null && gasPremium != null && feeCap != null;
    }
    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BigInteger getGasPremium() {
        return gasPremium;
    }

    public void setGasPremium(BigInteger gasPremium) {
        this.gasPremium = gasPremium;
    }

    public BigInteger getFeeCap() {
        return feeCap;
    }

    public void setFeeCap(BigInteger feeCap) {
        this.feeCap = feeCap;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }
}
