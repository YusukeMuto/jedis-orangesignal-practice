# jedis-orangesignal-sample

This is the Java console application.

## Redis server

You should start Redis server (Port: 6379) before running ResearchNearSpots.java as Java application.
```
$ redis-server
```

## アプリケーションの入出力仕様と利用方法
|ファイル名|説明|
|:-:|:-:|
|`nishisightseeing.csv`|入力ファイル|
|`NishiSightseeing.java`|入力csvファイルに対応するPOJO|
|`ResearchNearSpots.java`|アプリケーションのソース|
|`nishisightseeing_output.md`|出力ファイル(マークダウン形式)|

### 実行手順
1. 実行開始
2. データをcsvファイルから読み取り、redisへ保存する。
3. 現在地のX座標とY座標を**標準入力**する。
4. 現在地に近い上位5箇所の横浜市西区の観光スポットの名前・住所・解説を標準出力する。
5. **終了する**(7.に進む)[`$ end`]、**保存して終了する**(6.に進む)[`$ save`]、**もう一度検索を行う**(2.に戻る)[`$ その他の文字を入力`]
6. 現在地に近い順にソートしたすべての横浜市西区の観光スポットの名前・住所・解説を`nishisightseeing_output.md`ファイルに出力する。
7. 実行終了

### 現在地の入力に関して
現在地はdouble型で、x座標は[-21000,-18000]、y座標は[-62000,-59000]の範囲で入力する。

## 参考文献

#### 利用したオープンデータ

横浜市西区「観光スポット」のオープンデータ
http://www.city.yokohama.lg.jp/nishi/opendata/nishisightseeing.csv

(フォーマットの微調整を行っていない関係で、一部データを削除して利用している。)


#### OrangeSignal csv
http://orangesignal.osdn.jp/csv/userguide.html
http://orangesignal.osdn.jp/csv/api-release/index.html


#### Redis
https://github.com/antirez/redis
http://redis-documentasion-japanese.readthedocs.io/ja/latest/topics/data-types-intro.html

#### Jedis
Jedis
https://github.com/xetorthio/jedis
JedisのAPI
http://tool.oschina.net/uploads/apidocs/jedis-2.1.0/redis/clients/jedis/Jedis.html
SortedMap に関して
http://stackoverflow.com/questions/23056655/is-order-preserved-in-set-when-recovering-sorted-sets-using-jedis
