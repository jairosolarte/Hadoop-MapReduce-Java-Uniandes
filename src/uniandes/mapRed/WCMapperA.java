package uniandes.mapRed;

import java.io.IOException;
import java.util.HashMap;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WCMapperA extends Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		HashMap<String, Integer> palabrasLinea = new HashMap<String, Integer>();

		String[] palabras = value.toString().split("([().,!?:;'\"-]|\\s)+");

		for (String palabra : palabras) {
			String lw = palabra.toLowerCase().trim();

			if (lw.equals("")) {
				continue;
			} // No queremos contar espacios //Si la palabra

			if (lw.equals("</reuters>")) {
				palabrasLinea.put("Cantidad_noticias", palabrasLinea.containsKey("Cantidad_noticias") ? (palabrasLinea.get("Cantidad_noticias") + 1) : 1);
			}

		}
		
		for (String k : palabrasLinea.keySet()) {
			context.write(new Text(k), new IntWritable(palabrasLinea.get(k)));
		}

	}
}
