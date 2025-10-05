//
//  SecretsDecoder.swift
//  iosApp
//
//  Created by Calleb Joel Miquissene on 16.08.25.
//  Copyright © 2025 M-Pesa Multiplatform. All rights reserved.
//

import Foundation

enum Secrets {
    static var productionApiKey: String {
        guard let key = Bundle.main.infoDictionary?["MPESA_PRODUCTION_API_KEY"] as? String else {
            fatalError("M-PESA Production API Key is not found in Config.xcconfig")
        }
        return key
    }
    
    static var developmentApiKey: String {
        guard let key = Bundle.main.infoDictionary?["MPESA_DEVELOPMENT_API_KEY"] as? String else {
            fatalError("M-PESA Development API Key is not found in Config.xcconfig")
        }
        return key
    }
    
    static var publicApiKey: String {
        guard let key = Bundle.main.infoDictionary?["MPESA_PUBLIC_KEY"] as? String else {
            fatalError("M-PESA Public Key is not found in Config.xcconfig")
        }
        return key
    }
}
