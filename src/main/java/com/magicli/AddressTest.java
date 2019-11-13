package com.magicli;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.params.MainNetParams;
import org.web3j.crypto.Hash;

/**
 * Created by gnlyjy on 2019/11/5.
 */
public class AddressTest {
    public static void main(String[] args){
        NetworkParameters params = MainNetParams.get();
        ECKey key = ECKey.fromPrivate(Utils.HEX.decode("18e14a7b6a307f426a94f8114701e7c8e774e7f9a47e2c2035db29a206321725"),false);
        System.out.format("私钥 => %s\n", key.getPrivateKeyAsHex());
        System.out.format("公钥 => %s\n", key.getPublicKeyAsHex());
        System.out.format("地址 => %s\n", key.toAddress(params));

        System.out.println("============");

        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(MainNetParams.get(),"L4NVy3W3ZWpVZGLwVGoREQmrcAiBZgZ1uQcP8fzqBJYnRH1pgWEa");
        ECKey dumpPriKey = dumpedPrivateKey.getKey();
        System.out.format("私钥 => %s\n", dumpPriKey.getPrivateKeyAsHex());
        System.out.format("公钥 => %s\n", dumpPriKey.getPublicKeyAsHex());
        System.out.format("地址 => %s\n", dumpPriKey.toAddress(params));

        System.out.println("============");


        ECKey keyEth = ECKey.fromPrivate(Utils.HEX.decode("1f2b77e3a4b50120692912c94b204540ad44404386b10c615786a7efaa065d20"),false);
        System.out.format("私钥 => %s\n", keyEth.getPrivateKeyAsHex());

        byte[] pubKey = keyEth.getPubKey();
        byte[] pubKeyWithout04 = new byte[pubKey.length-1];
        System.arraycopy(pubKey,1,pubKeyWithout04,0,pubKeyWithout04.length);


        System.out.format("公钥(hex) => %s\n", Utils.HEX.encode(pubKey));
        System.out.format("公钥(去除0x04) => %s\n", Utils.HEX.encode(pubKeyWithout04));
        System.out.format("公钥 => %s\n", Utils.HEX.encode(Hash.sha3(keyEth.getPubKey())));
    }
}
