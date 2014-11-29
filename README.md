aws-sdk-java-ec2
================

EC2インスタンスを上げ下げするプログラムです。
呼び出しはシェルスクリプトやバッチファイルで行ってください。
例）
```Bash
java -classpath $CLASSPATH com.shimapee.ec2.Main com.shimapee.ec2.StartInstance
```

必要なライブラリ
- common-logging
- httpclient
- httpcore
- jackson-core
- jackson-annotations
- jackson-databind
- joda-time
- aws-java-sdk
