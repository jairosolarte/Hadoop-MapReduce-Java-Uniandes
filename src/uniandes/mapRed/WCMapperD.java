package uniandes.mapRed;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WCMapperD extends Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		HashMap<String, Integer> palabrasLinea = new HashMap<String, Integer>();

		
		
		String noticia = value.toString();
		String buscar = "billington";
		
		
			String[] dateN = noticia.split("<\\/DATE>|<DATE>");
			if (dateN.length > 1 && dateN[1] != null) {
				// do something
				String string = dateN[1];
				DateFormat format = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
				try {
					Date date = format.parse(string);
					Date dateH = format.parse("16-FEB-1987");
					Date dateF = format.parse("30-JUN-1987");
					if (date.after(dateH) && date.before(dateF)) {

						if (dateN.length > 2 && dateN[2] != null) {
							String[] palabras = dateN[2].split("([().,!?:;'\"-]|\\s)+");
							for (String palabra : palabras) {
								String lw = palabra.toLowerCase().trim();
								if (lw.equals("")) {
									continue;
								}
								if (lw.indexOf(buscar) > -1) {
									palabrasLinea.put(lw,
											palabrasLinea.containsKey(lw) ? (palabrasLinea.get(lw) + 1) : 1);
								}
							}
						}
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		

		
			for (String k : palabrasLinea.keySet()) {
			context.write(new Text(k), new IntWritable(palabrasLinea.get(k)));
		}

	}
}
