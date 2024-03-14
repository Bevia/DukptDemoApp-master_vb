package com.bevia;

import com.bevia.dukpt.ipek.BaseDerivationKey;
import com.bevia.dukpt.ipek.HexKeyComponent;
import com.bevia.dukpt.ipek.InitialPinEncryptionKey;
import com.bevia.genericterminal.GenericTerminalParser;
import com.bevia.genericterminal.GenericTerminalSwipeDataGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class DukptDemoApp implements CommandLineRunner
{

    public static final String KSN = "FFFF1000010000000007";

    @Override
    public void run(String... args) {
        BaseDerivationKey bdk = createBaseDerivationKey();
        InitialPinEncryptionKey ipek = new InitialPinEncryptionKey(bdk, KSN);
        GenericTerminalSwipeDataGenerator terminalSwipeDataGenerator = new GenericTerminalSwipeDataGenerator(ipek);
        String swipe = terminalSwipeDataGenerator.generateSwipe();
        String dek = terminalSwipeDataGenerator.getDataEncryptionKey();
        System.out.println("Generated Swipe Data: " + swipe);
        System.out.println("Data Encryption Key: " + dek);
        GenericTerminalParser parser = new GenericTerminalParser(swipe);
        LoggingData(parser);
    }

    private static void LoggingData(GenericTerminalParser parser) {
        System.out.println("Encrypted Track Two Data: "+ parser.getTrackTwoEncrypted());
        System.out.println("BDK: -----VB------->   " + createBaseDerivationKey());
    }

    public static void main(String[] args) {
        SpringApplication.run(DukptDemoApp.class, args);
    }

    private static BaseDerivationKey createBaseDerivationKey() {
        List<HexKeyComponent> keyComponents = new ArrayList<>();
        keyComponents.add(new HexKeyComponent("B3D97485261218FCA6B7B6E1AE55CDB0", "EB0DAE"));
        keyComponents.add(new HexKeyComponent("E097DC6ECD542E5E0D550AA6310ACA23", "477BE4"));
        keyComponents.add(new HexKeyComponent("23E54BBA6E6ECB69C48EAAC4EFFAE508", "6F1E19"));
        return new BaseDerivationKey(keyComponents, "161C16");
    }

    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }


}
