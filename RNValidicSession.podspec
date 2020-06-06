require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNValidicSession"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = "https://validic.com"
  s.license      = { :file => "LICENSE" }
  s.author       = ["Validic Mobile"]
  s.platform     = :ios, "10.0"
  s.source       = { :git => 'https://github.com/yesfit/react-native-validic-session.git', :tag => "v#{s.version}" }

  s.source_files =  "ios/Classes/*.{h,m}"
  s.requires_arc = true
  s.ios.vendored_frameworks = 'ios/ValidicCore.framework'
  s.framework = "ValidicCore"
  s.dependency "React"
  #s.dependency "others"
end

