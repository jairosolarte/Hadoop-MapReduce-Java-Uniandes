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

public class WCMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		HashMap<String, Integer> palabrasLinea = new HashMap<String, Integer>();

		// String[] palabras = value.toString().split("([().,!?:;'\"-]|\\s)+");
		// String[] palabras = value.toString().split("([().,!?:;'\"]|\\s)+");
		// String[] months = { "jan", "feb", "mar", "apr", "may", "jun", "jul",
		// "aug","sep","oct","nov","dec"};
		//String lw = value.toString(); 
		//palabrasLinea.put(lw, palabrasLinea.containsKey(lw) ? (palabrasLinea.get(lw)+1) :1);
		
		String noticia = value.toString();
		String buscar = "billington";
		//for (String noticia : noticias) {
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
		//}

		/*
		 * for(String palabra:palabras){ String lw=palabra.toLowerCase().trim();
		 * if(lw.equals("")){continue;} //No queremos contar espacios //Si la palabra
		 * existe en el hashmap incrementa en 1 su valor, //en caso contrario la agrega
		 * y le asigna 1.
		 * 
		 * if(lw.indexOf("<places>") > -1){ String[] pals = lw.split("\\s*(<[^>]*>)");
		 * for(String pal:pals){ if(pal.indexOf("<") == -1){ palabrasLinea.put(pal,
		 * palabrasLinea.containsKey(pal)? (palabrasLinea.get(pal)+1) :1); } } }
		 * 
		 * 
		 * 
		 * if(lw.indexOf("<date>") > -1){ for(int i=0; i<months.length; i++) {
		 * if(lw.indexOf(months[i])> -1) { palabrasLinea.put(months[i],
		 * palabrasLinea.containsKey(months[i])? (palabrasLinea.get(months[i])+1) :1); }
		 * } }
		 * 
		 * 
		 * 
		 * /*if(lw.equals("</reuters>")){ palabrasLinea.put(lw,
		 * palabrasLinea.containsKey(lw)? (palabrasLinea.get(lw)+1) :1); }
		 */
		/*
		 * palabrasLinea.put(lw, palabrasLinea.containsKey(lw)?
		 * (palabrasLinea.get(lw)+1) :1);
		 *//*
			 * }
			 */
		for (String k : palabrasLinea.keySet()) {
			context.write(new Text(k), new IntWritable(palabrasLinea.get(k)));
		}

	}
}
