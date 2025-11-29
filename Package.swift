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
            path: ".xcReleaseFramework/MpesaMultiplatformSdk.xcframework"
        ),
    ]
)
