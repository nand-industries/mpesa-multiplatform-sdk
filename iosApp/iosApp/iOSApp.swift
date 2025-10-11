import SwiftUI
import sdk

@main
struct iOSApp: App {
    init() {
        let productionApiKey = Secrets.productionApiKey
        let developmentApiKey = Secrets.developmentApiKey
        let publicApiKey = Secrets.publicApiKey
        
        MpesaMultiplatformSdkInitializer().doInit(
            productionApiKey: productionApiKey,
            developmentApiKey: developmentApiKey,
            publicKey: publicApiKey,
            isProduction: false,
            serviceProviderCode: "171717",
            rsaEncryptHelper: IOSRsaEncryptHelper()
        )
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

final class IOSRsaEncryptHelper: RsaEncryptHelper {
    func encrypt(apiKey: String, base64PublicKey: String) -> String {
        
        let rsaPublicKey = RSAPublicKey(pemEncoded: base64PublicKey)
        
        var error: Unmanaged<CFError>?
                
        let data = Data(apiKey.utf8)
                
        let encryptedData = SecKeyCreateEncryptedData(rsaPublicKey.key, .rsaEncryptionPKCS1, data as CFData, &error)! as Data
                
        return encryptedData.base64EncodedString()
    }
}

internal class RSAPublicKey {
    private(set) var key: SecKey!
    
    init(pemEncoded: String) {
        self.key = createPublicKey(keyString: pemEncoded)
    }
    
    private func createPublicKey(keyString: String) -> SecKey {
        let keyData = Data(base64Encoded: keyString.convertToCorrectFormat())!
        let sizeInBits = keyData.count * 8

        let keyDict: [CFString: Any] = [
            kSecAttrKeyType: kSecAttrKeyTypeRSA,
            kSecAttrKeyClass: kSecAttrKeyClassPublic,
            kSecAttrKeySizeInBits: NSNumber(value: sizeInBits),
            kSecReturnPersistentRef: true
        ]
        
        var error: Unmanaged<CFError>?
        var key: SecKey? = nil
        
        key = SecKeyCreateWithData(keyData as CFData, keyDict as CFDictionary, &error)
        
        return key!
    }
 }


extension String {
    func convertToCorrectFormat() -> String {
        let keyArray = self.components(separatedBy: "\n")
        
        var keyOutput: String = ""
        
        for item in keyArray {
            if !item.contains("-----") {
                keyOutput += item
            }
        }
        return keyOutput
    }
}
