# Devera SCORE 

This repository contains SCORE (Smart Contract for ICON) examples written in Java. 

## DeveraCourse SCORE

### build

```
cd deveracourse/
gradle optimizedJar
```

### deploy to local network

Initialize parameters:

- _tuitionFeePerStudent: tuition fee required for the course per student, student must transfer exact amount of the fee
- _durationInBlocks: course duration (for testing purpose I only set it 60 => 120s)
- _salaryForTeacherInICX: salary for the teacher of the course 
- _teacherAddress: teacher''s address of the course


```
goloop rpc --uri http://127.0.0.1:9082/api/v3 sendtx deploy ./deveracourse/build/libs/DeveraCourse-0.1.0-optimized.jar \
    --key_store ./godWallet.json --key_password gochain \
    --nid 0x3 --step_limit 2000000000 --content_type application/java \
    --param _tuitionFeePerStudent=1000000000000000000 \
    --param _durationInBlocks=60 \
    --param _salaryForTeacherInICX=10000000000000000000 \
    --param _teacherAddress=hx8efead2758636fe48d7a09051214e1361c312092
0x1fd20ed04380276b24979043e2576e4c61ee27fda00d6b845de79669533d8d8e

goloop rpc --uri http://127.0.0.1:9082/api/v3 txresult 0x1fd20ed04380276b24979043e2576e4c61ee27fda00d6b845de79669533d8d8e
{
  "to": "cx0000000000000000000000000000000000000000",
  "cumulativeStepUsed": "0x42418520",
  "stepUsed": "0x42418520",
  "stepPrice": "0x2e90edd00",
  "eventLogs": [],
  "logsBloom": "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
  "status": "0x1",
  "scoreAddress": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
  "blockHash": "0x8ba68508070240e626ef90dc41f81608e3028303e61a37b376c3d6765a4906d6",
  "blockHeight": "0x7e",
  "txIndex": "0x0",
  "txHash": "0x1cbafb5b1339589cea14da367804fce7b0c25a7328326ef824cad53ed9699e00"
}
```

Use gradle javaee plugin to deploy: 
```
./gradlew deveracourse:deployToLocal -PkeystoreName=./godWallet.json -PkeystorePass=gochain

> Task :deveracourse:deployToLocal
>>> deploy to http://localhost:9082/api/v3
>>> target address = cx7d16effa95bc8de095499c06962864d3f05dca5d
>>> optimizedJar = /home/hayden/bai8/DeveraCourse/icon/SCORE/deveracourse/build/libs/DeveraCourse-0.1.0-optimized.jar
>>> keystore = ./godWallet.json
Succeeded to deploy: 0x0de531f490f7f9d0f9e2963f74a05cc5258f2ab7cb61aec2e0558fe322b265fc
SCORE address: cx7d16effa95bc8de095499c06962864d3f05dca5d

```
## iconFoundation send 10 ICX to active the course
SCORE Address:cx7d16effa95bc8de095499c06962864d3f05dca5d
```
goloop rpc --uri http://127.0.0.1:9082/api/v3 sendtx transfer --to cxe8ef69379e2b2c63a72ded5cc9baf6f1d1d37ebb --value 10000000000000000000 --key_store ./godWallet.json --key_password gochain --nid 0x3 --step_limit 1000000000
0xca560cafa8d1d95d4428cf57c39c783e9bd5e85257ad88779cb8d64878dc336c

goloop rpc --uri http://127.0.0.1:9082/api/v3 txresult 0xca560cafa8d1d95d4428cf57c39c783e9bd5e85257ad88779cb8d64878dc336c
{
  "to": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
  "cumulativeStepUsed": "0x3822c",
  "stepUsed": "0x3822c",
  "stepPrice": "0x2e90edd00",
  "eventLogs": [
    {
      "scoreAddress": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
      "indexed": [
        "CourseStarted(int,int)"
      ],
      "data": [
        "0xde0b6b3a7640000",
        "0x3c"
      ]
    }
  ],
  "logsBloom": "0x00000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000000000004000004000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000080000000000000000000000000000000",
  "status": "0x1",
  "blockHash": "0xd7e2f0bc690952ef17e862313aff46f56faf1e6f3d8e90c818e427558b717a96",
  "blockHeight": "0xae",
  "txIndex": "0x0",
  "txHash": "0xca560cafa8d1d95d4428cf57c39c783e9bd5e85257ad88779cb8d64878dc336c"
}
```
## Transfer 100 ICX to: 2 students and teacher
these ICX will be used for transfering tuition fee and paying transaction fund

```
goloop rpc --uri http://127.0.0.1:9082/api/v3 sendtx transfer --to hxf4b3bc606c183d86ed61fdd843ef28e56e3bb9dd --value 100000000000000000000 --key_store godWallet.json --key_password gochain --nid 0x3 --step_limit 1000000

goloop rpc --uri http://127.0.0.1:9082/api/v3 sendtx transfer --to hx1709cdb7da0c0787eeb19070db00427013fec007 --value 100000000000000000000 --key_store godWallet.json --key_password gochain --nid 0x3 --step_limit 1000000

goloop rpc --uri http://127.0.0.1:9082/api/v3 sendtx transfer --to hx8efead2758636fe48d7a09051214e1361c312092 --value 100000000000000000000 --key_store godWallet.json --key_password gochain --nid 0x3 --step_limit 1000000
```

## 2 students transfer 1 ICX to register the course
```
goloop rpc --uri http://127.0.0.1:9082/api/v3 sendtx transfer --to cx7d16effa95bc8de095499c06962864d3f05dca5d --value 1000000000000000000 --key_store ./Nhi.json --key_password nhi123456 --nid 0x3 --step_limit 1000000000
0x0fdff57a008f5e1ffdb0ca2d3435d895239f7b867180061f69a119a6b75bffe0

goloop rpc --uri http://127.0.0.1:9082/api/v3 txresult 0x0fdff57a008f5e1ffdb0ca2d3435d895239f7b867180061f69a119a6b75bffe0
{
  "to": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
  "cumulativeStepUsed": "0x249e9",
  "stepUsed": "0x249e9",
  "stepPrice": "0x2e90edd00",
  "eventLogs": [
    {
      "scoreAddress": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
      "indexed": [
        "StudentSignedCourse(Address,int)",
        "hxf4b3bc606c183d86ed61fdd843ef28e56e3bb9dd",
        "0xde0b6b3a7640000"
      ],
      "data": []
    }
  ],
  "logsBloom": "0x00000000000000000000004000000000000000000000000000000008000000000000000000000000000000000000800000000000000000000000008004000000000000000000000000000000000000000080000000002000200000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008000000000000004008000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000000000000000",
  "status": "0x1",
  "blockHash": "0x6c274702ffcb0c1051eaad6d44e2affc4047b1cd8b9422faf81ee4d51a3d36fe",
  "blockHeight": "0xbf",
  "txIndex": "0x0",
  "txHash": "0x0fdff57a008f5e1ffdb0ca2d3435d895239f7b867180061f69a119a6b75bffe0"
}

```

the other student send 1 ICX to register the course
```
goloop rpc --uri http://127.0.0.1:9082/api/v3 sendtx transfer --to cx7d16effa95bc8de095499c06962864d3f05dca5d --value 1000000000000000000 --key_store ./Tien.json --key_password tien123456 --nid 0x3 --step_limit 1000000000
```
## Check tuitionRaised of the course 
```
goloop rpc --uri http://127.0.0.1:9082/api/v3 call --to cx7d16effa95bc8de095499c06962864d3f05dca5d --method tuitionRaised
```

## Student prove attendance 
# MUST call 3 times to prove enough presentation in class 

```
goloop rpc --uri http://127.0.0.1:9082/api/v3 sendtx call --to cx7d16effa95bc8de095499c06962864d3f05dca5d --method proveAttendance \
  --key_store ./Nhi.json --key_password nhi123456 \
  --nid 0x3 --step_limit 2000000000

0x60095598a8046347c68975c4527d7f7f54a7c99b3a1f7fd7ef6f471023c66bf0

goloop rpc --uri http://127.0.0.1:9082/api/v3 txresult 0x60095598a8046347c68975c4527d7f7f54a7c99b3a1f7fd7ef6f471023c66bf0
{
  "to": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
  "cumulativeStepUsed": "0x258fd",
  "stepUsed": "0x258fd",
  "stepPrice": "0x2e90edd00",
  "eventLogs": [
    {
      "scoreAddress": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
      "indexed": [
        "StudentProveAttendance(Address,int)",
        "hxf4b3bc606c183d86ed61fdd843ef28e56e3bb9dd",
        "0x3"
      ],
      "data": []
    }
  ],
  "logsBloom": "0x00000000000000000000000000000000000000000000020000000008000000000000000000000000000000000000800000400000000000000000400004000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008080000000000000000000000000000000000000000000000080000000000000000000000000000000000000000000000000000000000000000000000000000000000000000020000000000000000000008000000000000000000000000000000000000000000000000000000000000000",
  "status": "0x1",
  "blockHash": "0x46eff1031babbbdfc0ad0da01b2a912ad6f9658ff43ef281658a2cb1c2b49f74",
  "blockHeight": "0x107",
  "txIndex": "0x0",
  "txHash": "0x60095598a8046347c68975c4527d7f7f54a7c99b3a1f7fd7ef6f471023c66bf0"
}
```
=> 0x3 is 3 times presenting in class => student can withdral when the course finish

## Student withdraw tuition after the course
```
goloop rpc --uri http://127.0.0.1:9082/api/v3 sendtx call --to cx7d16effa95bc8de095499c06962864d3f05dca5d --method studentWithdrawal \
  --key_store ./Nhi.json --key_password nhi123456 \
  --nid 0x3 --step_limit 2000000000
0x22574e7e8cd98e0a200d9890100c70ede136e410526a09301dbab964786660e3

goloop rpc --uri http://127.0.0.1:9082/api/v3 txresult 0x22574e7e8cd98e0a200d9890100c70ede136e410526a09301dbab964786660e3
{
  "to": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
  "cumulativeStepUsed": "0x28688",
  "stepUsed": "0x28688",
  "stepPrice": "0x2e90edd00",
  "eventLogs": [
    {
      "scoreAddress": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
      "indexed": [
        "ICXTransfer(Address,Address,int)",
        "cx7d16effa95bc8de095499c06962864d3f05dca5d",
        "hxf4b3bc606c183d86ed61fdd843ef28e56e3bb9dd",
        "0xde0b6b3a7640000"
      ],
      "data": []
    },
    {
      "scoreAddress": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
      "indexed": [
        "StudentWithdrawal(Address,int,int)",
        "hxf4b3bc606c183d86ed61fdd843ef28e56e3bb9dd",
        "0xde0b6b3a7640000",
        "0x3"
      ],
      "data": []
    }
  ],
  "logsBloom": "0x00000000400000000000000100000000000000000000000000000008000000000000000000000000000000000104800000000000000000000000088004000000000000000000000000000000000000000000200000012000280000000000000000000000000000000000000000000000000000000000200020000400000000000000000000000000000000000004008000000000000000000000002000000000002000000000000000100000000000000000000000080000000000000000000000000000000008000000000000000000000000000000000000000000000000008000000000200000000000000000000000010000000000000000000000000000",
  "status": "0x1",
  "blockHash": "0x445c76563fea09b5b98e4f6d3b54653396372eb45e8df19a22e08bd692b80d5d",
  "blockHeight": "0x116",
  "txIndex": "0x0",
  "txHash": "0x22574e7e8cd98e0a200d9890100c70ede136e410526a09301dbab964786660e3"
}
```

## Teacher can withdraw after the course finish and the course will be inactive
```
goloop rpc --uri http://127.0.0.1:9082/api/v3 sendtx call --to cx7d16effa95bc8de095499c06962864d3f05dca5d --method teacherWithdrawal \
  --key_store ./thayQuy.json --key_password quy123456 \
  --nid 0x3 --step_limit 2000000000
0xa54217a768c6db57d4a537207e61018625a478fd71002c0d93109d3e3a960878

goloop rpc --uri http://127.0.0.1:9082/api/v3 txresult 0xa54217a768c6db57d4a537207e61018625a478fd71002c0d93109d3e3a960878
{
  "to": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
  "cumulativeStepUsed": "0x3e0e6",
  "stepUsed": "0x3e0e6",
  "stepPrice": "0x2e90edd00",
  "eventLogs": [
    {
      "scoreAddress": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
      "indexed": [
        "ICXTransfer(Address,Address,int)",
        "cx7d16effa95bc8de095499c06962864d3f05dca5d",
        "hx8efead2758636fe48d7a09051214e1361c312092",
        "0x8ac7230489e80000"
      ],
      "data": []
    },
    {
      "scoreAddress": "cx7d16effa95bc8de095499c06962864d3f05dca5d",
      "indexed": [
        "TeacherWithdrawal(Address,int)",
        "hx8efead2758636fe48d7a09051214e1361c312092",
        "0x8ac7230489e80000"
      ],
      "data": []
    }
  ],
  "logsBloom": "0x00000000000000000000000100000000000000000000000080000808000000000000000002000080000000000000000000000000000000008040080004000000001000000000000000000200000000000000000000000000080000100000000000000000000000000000108000000000000000000000200000000000000000000000000000008000000000000000008000000000000000000000002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000040000000000000000000040000000000000000000000000000200000000000000000000000000000000000000000000000000000",
  "status": "0x1",
  "blockHash": "0xff329e96a11d0a3f2cbe65580452729d9da5af8cebba169b5d6189bfe07ffbe1",
  "blockHeight": "0x17a",
  "txIndex": "0x0",
  "txHash": "0xa54217a768c6db57d4a537207e61018625a478fd71002c0d93109d3e3a960878"
}
```