package uniandes.mapRed;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WCMapperB extends Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		HashMap<String, Integer> palabrasLinea = new HashMap<String, Integer>();

		String[] palabras = value.toString().split("([().,!?:;'\"]|\\s)+");
		String[] months = { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };

		for (String palabra : palabras) {
			String lw = palabra.toLowerCase().trim();

			if (lw.equals("")) {
				continue;
			} // No queremos contar espacios //Si la palabra

			if (lw.indexOf("<date>") > -1) {
				for (int i = 0; i < months.length; i++) {
					if (lw.indexOf(months[i]) > -1) {
						palabrasLinea.put(months[i],
								palabrasLinea.containsKey(months[i]) ? (palabrasLinea.get(months[i]) + 1) : 1);
					}
				}
			}

		}
		// String[] palabras = value.toString().split("([().,!?:;'\"]|\\s)+");
		for (String k : palabrasLinea.keySet()) {
			context.write(new Text(k), new IntWritable(palabrasLinea.get(k)));
		}

	}
}
