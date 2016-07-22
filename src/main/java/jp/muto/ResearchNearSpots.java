package jp.muto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

import com.orangesignal.csv.Csv;
import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.handlers.CsvEntityListHandler;
/*
import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.filters.SimpleBeanFilter;
import com.orangesignal.csv.handlers.BeanListHandler;
*/

/*
 * 西横浜の近場の観光地を検索する。
 */
public class ResearchNearSpots {

	public ResearchNearSpots() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {
			/**
			 * csvファイル読み込み
			 */
			File inputCsvFile = new File("nishisightseeing.csv");
			CsvConfig config = new CsvConfig();
			config.setIgnoreEmptyLines(true);
			CsvEntityListHandler<NishiSightseeing> csvHandler = new CsvEntityListHandler<>(NishiSightseeing.class);
			List<NishiSightseeing> nishiSsList = Csv.load(inputCsvFile, "UTF-8", config, csvHandler);

			/**
			 * Jedis jedis = new Jedis("zest.comp.ae.keio.ac.jp");
			 * jedis.auth("ap2016"); サーバーにうまく接続できなかったので、localhost:6379 で実行しました。
			 */
			Jedis jedis = new Jedis("localhost", 6379);

			/**
			 * Jedis へデータを保存
			 */
			String key;
			for (NishiSightseeing n : nishiSsList) {
				key = n.getName();
				Map<String, String> value = new HashMap<>();
				value.put("住所", n.getAddress());
				value.put("解説", n.getExplanation());
				value.put("X座標", String.valueOf(n.getPositionX()));
				value.put("Y座標", String.valueOf(n.getPositionY()));
				jedis.hmset(key, value);
			}

			/**
			 * 繰り返し検索準備
			 */
			Scanner scan = new Scanner(System.in);
			Double value;
			String scoreKey = "nearSpots";

			/**
			 * 繰り返し検索ループ
			 */
			while (true) {
				System.out.println("現在地を入力してください。");
				System.out.print("x座標(double)[-21000,-18000] : ");
				Double inputPosX = scan.nextDouble();
				System.out.print("y座標(double)[-62000,-59000] : ");
				Double inputPosY = scan.nextDouble();

				/**
				 * Jedis の Sorted Set を利用して、現在地に近い順に観光地をソート
				 */
				Map<String, Double> scoreMembers = new HashMap<>();
				for (NishiSightseeing n : nishiSsList) {
					key = n.getName();
					Double posX = Double.parseDouble(jedis.hget(key, "X座標"));
					Double posY = Double.parseDouble(jedis.hget(key, "Y座標"));
					value = Math.sqrt(Math.pow(inputPosX - posX, 2) + Math.pow(inputPosY - posY, 2));
					scoreMembers.put(key, value);
				}
				jedis.zadd(scoreKey, scoreMembers);

				/**
				 * 標準出力にもっとも近い5個の観光地を出力
				 */
				int showNum = 5;
				int counter = 0;
				Set<String> results = jedis.zrange(scoreKey, 0, showNum - 1);
				for (String s : results) {
					counter++;
					System.out.println(counter + ". " + s + "（距離=" + jedis.zscore(scoreKey, s) + ")");
					System.out.println("　住所: " + jedis.hget(s, "住所"));
					System.out.println("　解説: " + jedis.hget(s, "解説"));
				}

				/**
				 * 終了する・結果を保存して終了する・もう一度検索するの分岐
				 */
				System.out.println("Please input \"end\" [終了], \"save\" [結果を保存して終了], otherwise research again");
				String input = scan.next();

				if (input.equals("end")) {
					/**
					 * 終了するの場合の処理
					 */
					scan.close();
					break;

				} else if (input.equals("save")) {
					/**
					 * 保存するの場合の処理
					 */
					/** マークダウン形式で出力 **/
					Path path = Paths.get("nishisightseeing_output.md");
					BufferedWriter bw = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
					bw.write("# 現在地 (" + inputPosX + ", " + inputPosY + ") から近い観光スポット");
					bw.newLine();
					Set<String> allResults = jedis.zrange(scoreKey, 0, -1);
					int number = 0;
					for (String s : allResults) {
						number++;
						bw.write("## " + number + ". " + s + "（距離= " + jedis.zscore(scoreKey, s) + ")");
						bw.newLine();
						bw.write("### 住所");
						bw.newLine();
						bw.write(jedis.hget(s, "住所"));
						bw.newLine();
						bw.write("### 解説");
						bw.newLine();
						bw.write(jedis.hget(s, "解説"));
						bw.newLine();
					}
					bw.close();
					/*
					 * beanlisthandler を使いたい場合。
					 * BeanListHandler<NishiSightseeing> beanListHandler = new
					 * BeanListHandler<>(NishiSightseeing.class).includes(
					 * "name", "address", "explanation"); SimpleBeanFilter
					 * filter = new SimpleBeanFilter(); filter.le("positionX",
					 * input); beanListHandler.header(true);
					 * beanListHandler.filter(filter); Csv.save(nishiSsList,
					 * outputCsvFile, "UTF-8", config, beanListHandler);
					 */
					break;
				}
			}
			/**
			 * Jedis の掃除
			 */
			jedis.del(scoreKey);
			jedis.flushAll();
			jedis.close();

			System.out.println("終了しました");

		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

}
