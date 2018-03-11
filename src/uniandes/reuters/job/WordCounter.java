package uniandes.reuters.job;

import java.io.IOException;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import uniandes.mapRed.WCMapper;
import uniandes.mapRed.WCMapperA;
import uniandes.mapRed.WCMapperB;
import uniandes.mapRed.WCMapperC;
import uniandes.mapRed.WCMapperD;
import uniandes.mapRed.WCReducer;


public class WordCounter {
	public static void main(String[] args)  {
		if(args.length<2){
			System.out.println("Se necesitan las carpetas de entrada y salida");
			System.exit(-1);
		}
		System.out.println("------>"+args[0]+"--->"+args[1]);
		String entrada = args[0]; //carpeta de entrada
		String salida = args[1];//La carpeta de salida no puede existir
		String fn = args[2];//La carpeta de salida no puede existir
		
		try {
			ejecutarJob(entrada, salida, fn);
		} catch (Exception e) { //Puede ser IOException, ClassNotFoundException o InterruptedException
			e.printStackTrace();
		} 
			
	}
	public static void ejecutarJob(String entrada, String salida, String fn) throws IOException,ClassNotFoundException, InterruptedException
	{
		/**
		 * Objeto de configuración, dependiendo de la versión de Hadoop 
		 * uno u otro es requerido. 
		 * */
		Configuration conf = new Configuration();
		if(fn.equals("d")){
			conf.set("textinputformat.record.delimiter", "</REUTERS>");
		} 
		Job wcJob=Job.getInstance(conf, "WordCounter Job");
		wcJob.setJarByClass(WordCounter.class);
		//////////////////////
		//Mapper
		//////////////////////
		//wcJob.setMapperClass(WCMapperA.class);	
		
		if(fn.equals("a")) {
			wcJob.setMapperClass(WCMapperA.class);
		}else if(fn.equals("b") ){
			wcJob.setMapperClass(WCMapperB.class);
		}else if(fn.equals("c") ) {
			wcJob.setMapperClass(WCMapperC.class);
		}else if(fn.equals("d") ) {			
			wcJob.setMapperClass(WCMapperD.class);
		}else {
			conf.set("textinputformat.record.delimiter", "</REUTERS>");
			wcJob.setMapperClass(WCMapper.class); 
		}
	
				
		wcJob.setMapOutputKeyClass(Text.class);
		wcJob.setMapOutputValueClass(IntWritable.class);
		///////////////////////////
		//Reducer
		///////////////////////////
		wcJob.setReducerClass(WCReducer.class);
		wcJob.setOutputKeyClass(Text.class);
		wcJob.setOutputValueClass(IntWritable.class);
		
		///////////////////////////
		//Input Format
		///////////////////////////
		//Advertencia: Hay dos clases con el mismo nombre, 
		//pero no son equivalentes. 
		//Se usa, en este caso, org.apache.hadoop.mapreduce.lib.input.TextInputFormat
		TextInputFormat.setInputPaths(wcJob, new Path(entrada));
		wcJob.setInputFormatClass(TextInputFormat.class); 
		
		////////////////////
		///Output Format
		//////////////////////
		TextOutputFormat.setOutputPath(wcJob, new Path(salida));
		wcJob.setOutputFormatClass(TextOutputFormat.class);
		wcJob.waitForCompletion(true);
		System.out.println(wcJob.toString());
	}
}
