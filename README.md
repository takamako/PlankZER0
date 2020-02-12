
# enpit2019_v.i.p

enPiT2019のリポジトリです。

チーム名:vip

サービス名:plankZER0

## エレベータービッチ



``部活中で正しいフォームでプランクトレーニングしたい``

``部活生向け``の

``plankZER0``は

``プランクトレーニング用アプリ``です

既存のアプリなどは、タイマー、日程管理、アニメーションなどが付いているが、

このアプリは``姿勢判定機能``がついていて、``正しいフォームではないと警告音がなる``

これによって、``正しい姿勢での体幹トレーニング``ができる

+ プランクトレーニングとは、
両手両足を肩幅に開く
肘とつま先の４点に重点を置き、肘から下で補助的に体を支える
背中のラインが真っすぐなるように
の姿勢を何秒かキープする

## 実行環境のURL

+ プロダクトバックログのURL

https://docs.google.com/spreadsheets/d/1eZbUSZB0zCzLx7_7K2IpM9xiXFGm2x754NFPzddrOB0/edit#gid=891834841

+ 話し合い

https://hackmd.io/0kVWkFYpR1GFhtapA0Bsjw

+ 具体的なドキュメントのURL

https://hackmd.io/UG0VPO1yQjeqhTAwLyNLYg

Wikiページを作る

## ワーニングアグリーメント

遅刻の場合->連絡なく遅れた場合は他のメンバーに何か奢る
          何分遅れるかなどを申告する

連絡手段->slack (見たらリアクションつける)

決定の方法->多数決

Gitのルール->masterの直pushはみんな集まってる時のみで、それ以外はbranch

リモート時:説明責任を果たす(readに書く、コードに解説書くなど)

## LICENSE
このアプリには[tensorflow/examples](https://github.com/tensorflow/examples)を使用しています



## Build the demo using Android Studio

### Prerequisites

* If you don't have it already, install **[Android Studio](
 https://developer.android.com/studio/index.html)** 3.2 or
 later, following the instructions on the website.

* Android device and Android development environment with minimum API 21.

### Building
* Open Android Studio, and from the `Welcome` screen, select
`Open an existing Android Studio project`.

* From the `Open File or Project` window that appears, navigate to and select
 the `PlankZer0` directory from wherever you
 cloned the PlankZer0 GitHub repo. Click `OK`.

* If it asks you to do a `Gradle Sync`, click `OK`.

* You may also need to install various platforms and tools, if you get errors
 like `Failed to find target with hash string 'android-21'` and similar. Click
 the `Run` button (the green arrow) or select `Run` > `Run 'android'` from the
 top menu. You may need to rebuild the project using `Build` > `Rebuild Project`.

* If it asks you to use `Instant Run`, click `Proceed Without Instant Run`.

* Also, you need to have an Android device plugged in with developer options
 enabled at this point. See **[here](
 https://developer.android.com/studio/run/device)** for more details
 on setting up developer devices.


### Model used
Downloading, extraction and placement in assets folder has been managed
 automatically by `download.gradle`.

If you explicitly want to download the model, you can download it from
 **[here](
 https://storage.googleapis.com/download.tensorflow.org/models/tflite/posenet_mobilenet_v1_100_257x257_multi_kpt_stripped.tflite)**.

### Additional Note
_Please do not delete the assets folder content_. If you explicitly deleted the
 files, then please choose `Build` > `Rebuild` from menu to re-download the
 deleted model files into assets folder.
