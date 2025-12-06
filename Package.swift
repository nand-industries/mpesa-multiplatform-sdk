// swift-tools-version:5.9

//
// Created by Calleb Joel Miquissene on 29.11.25.
//

import PackageDescription

let package = Package(
    name: "MpesaMultiplatformSdk",
    platforms: [
        .iOS(.v14),
    ],
    products: [
        .library(name: "MpesaMultiplatformSdk", targets: ["MpesaMultiplatformSdk"])
    ],
    targets: [
        .binaryTarget(
            name: "MpesaMultiplatformSdk",
            url: "https://github.com/nand-industries/mpesa-multiplatform-sdk/releases/download/2.0.1/MpesaMultiplatformSdk.xcframework.zip",
            checksum: "a7bee71854bfec181b4efb2d0445303079d08da67a6b41bc6e3db2624d6b8f04"
        ),
    ]
)
