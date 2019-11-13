package com.magicli;

import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.*;
import org.bitcoinj.wallet.DeterministicSeed;
import org.junit.Test;
import org.web3j.abi.DefaultFunctionEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.ens.contracts.generated.PublicResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.web3j.tx.Transfer.GAS_LIMIT;

/**
 * Created by gnlyjy on 2019/11/12.
 */
public class Web3Test {
    @Test
    public void TestGetTransactionCount() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/5c1324dbb0594ceabf05ed3b1234f838"));

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                "0x46f7B279265C8Cd52bf43288BCf7392d7EaD49dF", DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        System.out.println(nonce);
    }

    @Test
    public void TestGetBalance() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/5c1324dbb0594ceabf05ed3b1234f838"));

        EthGetBalance ethGetBalance = web3j.ethGetBalance("0x46f7B279265C8Cd52bf43288BCf7392d7EaD49dF", DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger balance = ethGetBalance.getBalance();
        System.out.println(balance);
    }

    @Test
    public void TestGetGasPrice() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/5c1324dbb0594ceabf05ed3b1234f838"));

        EthGasPrice gasPrice = web3j.ethGasPrice().sendAsync().get();
        BigInteger gs = gasPrice.getGasPrice();
        System.out.println(gs);
    }


    @Test
    public void TestCall() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/5c1324dbb0594ceabf05ed3b1234f838"));


        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Uint256 unit256 = new Uint256(BigInteger.valueOf(1000000000L));
        inputParameters.add(unit256);
        Function f = new Function("withdraw", inputParameters, outputParameters);
        String data = DefaultFunctionEncoder.encode(f);


        // create our transaction
        RawTransaction rawTransaction = RawTransaction.createTransaction(BigInteger.valueOf(8), BigInteger.valueOf(1000000000), BigInteger.valueOf(312520), "0xf2146b6ddf2b7844e6ab2a63bcca66b70353ca69", data);

        // sign & send our transaction
        //0x46f7B279265C8Cd52bf43288BCf7392d7EaD49dF
        byte[] priKey = Utils.HEX.decode("a6ea51bb9c645607c3e5eb6c7e91ba68a8b8075a64cb0a26bb8a45fcc796a881");
        ECKeyPair ecKeyPair = ECKeyPair.create(priKey);

        Credentials credentials = Credentials.create(ecKeyPair);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, 3, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        System.out.println(ethSendTransaction.getTransactionHash());
        System.out.println(ethSendTransaction.getResult());

        System.out.println(ethSendTransaction.getError().getMessage());

    }

    @Test
    public void TestTransfer() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/5c1324dbb0594ceabf05ed3b1234f838"));



        // create our transaction
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(BigInteger.valueOf(7), BigInteger.valueOf(1000000000), BigInteger.valueOf(312520), "0xf2146b6ddf2b7844e6ab2a63bcca66b70353ca69", BigInteger.valueOf(10000));

        // sign & send our transaction
        //0x46f7B279265C8Cd52bf43288BCf7392d7EaD49dF
        byte[] priKey = Utils.HEX.decode("a6ea51bb9c645607c3e5eb6c7e91ba68a8b8075a64cb0a26bb8a45fcc796a881");
        ECKeyPair ecKeyPair = ECKeyPair.create(priKey);

        Credentials credentials = Credentials.create(ecKeyPair);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, 3, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        System.out.println(ethSendTransaction.getTransactionHash());
        System.out.println(ethSendTransaction.getResult());
        System.out.println(ethSendTransaction.getError().getMessage());

    }

    @Test
    public void TestCall1() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/5c1324dbb0594ceabf05ed3b1234f838"));

        byte[] priKey = Utils.HEX.decode("a6ea51bb9c645607c3e5eb6c7e91ba68a8b8075a64cb0a26bb8a45fcc796a881");
        ECKeyPair ecKeyPair = ECKeyPair.create(priKey);

        Credentials credentials = Credentials.create(ecKeyPair);


        Contract contract = PublicResolver.load("0xf2146b6ddf2b7844e6ab2a63bcca66b70353ca69",web3j,credentials, BigInteger.valueOf(1000000000L),BigInteger.valueOf(1000000000L));
//        contract.

//        EthSendRawTransaction

    }

    @Test
    public void EthAddressTest() throws Exception {

        byte[] priKey = Utils.HEX.decode("a6ea51bb9c645607c3e5eb6c7e91ba68a8b8075a64cb0a26bb8a45fcc796a881");
        ECKeyPair keyPair = ECKeyPair.create(priKey);
//通过公钥生成钱包地址
        String address = Keys.getAddress(keyPair.getPublicKey());

        System.out.println("地址：");
        System.out.println("0x" + address);
        System.out.println();
        System.out.println("私钥：");
        System.out.println("0x" + keyPair.getPrivateKey().toString(16));
        System.out.println();
        System.out.println("公钥：");
        System.out.println(keyPair.getPublicKey().toString(16));
    }

    @Test
    public void HashTest() {
//        私钥：1f2b77e3a4b50120692912c94b204540ad44404386b10c615786a7efaa065d20
//        公钥：04dfa13518ff965498743f3a01439dd86bc34ff9969c7a3f0430bbf8865734252953c9884af787b2cadd45f92dff2b81e21cfdf98873e492e5fdc07e9eb67ca74d
//        地址：0xabcd68033A72978C1084E2d44D1Fa06DdC4A2d57
        byte[] priKey = Utils.HEX.decode("1f2b77e3a4b50120692912c94b204540ad44404386b10c615786a7efaa065d20");
        ECKeyPair keyPair = ECKeyPair.create(priKey);
        byte[] pubKey = keyPair.getPublicKey().toByteArray();
//        System.out.println(pubKey.length);
        String pubKeyStr = Utils.HEX.encode(pubKey);
        System.out.println(pubKeyStr);//00dfa13518ff965498743f3a01439dd86bc34ff9969c7a3f0430bbf8865734252953c9884af787b2cadd45f92dff2b81e21cfdf98873e492e5fdc07e9eb67ca74d

        //搞什么鬼啊，竟然是用pubKey的hexString去哈希的？？？
        System.out.println(Hash.sha3(pubKeyStr.substring(2)));//0x39c0eb3b26d4838930b1f34babcd68033a72978c1084e2d44d1fa06ddc4a2d57
    }
}
