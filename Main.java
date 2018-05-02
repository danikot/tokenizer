package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//read each after two tabs
		BufferedReader FirstAnnotationBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[0]))));
		BufferedReader SecondAnnotationBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[1]))));
		
		System.out.println("extracting map of annotations 1");
		Map Annotations1=ExtractMapOfAnnotations(FirstAnnotationBufferedReader);
		System.out.println("extracting map of annotations 2");
		Map Annotations2=ExtractMapOfAnnotations(SecondAnnotationBufferedReader);
		FirstAnnotationBufferedReader.close();
		SecondAnnotationBufferedReader.close();
		int intTotalUnits=Integer.min(Annotations1.keySet().size(), Annotations2.keySet().size());	

		System.out.println("Total units: " + intTotalUnits);
		System.out.println("calculating Uebereinstimmung");
		int intUebereinstimmung=Uebereinstimmung(Annotations1,Annotations2,intTotalUnits);
		System.out.println("Uebereinstimmungen: " + intUebereinstimmung);
		float floatProzentualeUebereinstummung=((float)intUebereinstimmung / intTotalUnits) * 100;
		System.out.println("Prozentuale Uebereinstimmung: " + floatProzentualeUebereinstummung + "%.");
		
		float floatKappaUebereinstimmung=Kappa(Annotations1,Annotations2,intTotalUnits,intUebereinstimmung);
		System.out.println("Kappa: " + -floatKappaUebereinstimmung +".");
	}

	private static float Kappa(Map annotations1, Map annotations2,int TotalUnits, int intUebereinstimmung) {
		float p0=(float)intUebereinstimmung / TotalUnits;
		//System.out.println("p0 is " + p0);
		float pc= getPc(annotations1,annotations2,TotalUnits);
		//System.out.println("pc is " + pc);
		float kappa=(p0*pc) / (1-pc); 
		return kappa/2;
	}

	private static float getPc(Map annotations1, Map annotations2,int TotalUnits) {
		float Pc=(float) 0;
		for (Object x:new HashSet(annotations1.values()))
		{//System.out.println("freq of " + x + "in a1 is" + Collections.frequency(annotations1.values(),x));

		//System.out.println("freq of " + x + "in a2 is" + Collections.frequency(annotations2.values(),x));
		if(Collections.frequency(annotations2.values(),x)!=0)	
		{Pc=Pc+
		(((float)Collections.frequency(annotations1.values(),x)/TotalUnits)
		+
		((float)Collections.frequency(annotations2.values(),x))/TotalUnits);}}
	
		return Pc;
	}

	private static int Uebereinstimmung(Map annotations1, Map annotations2, int TotalUnits) {
		int counter=0;
		int Uebereinstimmungen=0;
		
		while(counter<TotalUnits)
		{   System.out.print(annotations2.get(counter) + " -|- ");
			System.out.print(annotations1.get(counter) + "\n");
			if(annotations1.get(counter).equals(annotations2.get(counter)))
		    {   //System.out.println("equals!");
				Uebereinstimmungen++;}
			counter++;}
		
		return Uebereinstimmungen;
	}

	private static Map<Integer, String> ExtractMapOfAnnotations(BufferedReader Reader) throws IOException {
		String Zeile;
		int key=0;
		Map<Integer, String> AnnotationsMap=new HashMap<Integer, String>();
		while((Zeile=Reader.readLine()) != null){
			System.out.println(Zeile);
			String ZeileAnnotation=GetAnnotation(Zeile);
		if(ZeileAnnotation!="not annotation line"){AnnotationsMap.put(key, ZeileAnnotation); key++;}
		}
		return AnnotationsMap;
	}

	private static String GetAnnotation(String LineFromFile) {
		String Annotation="not annotation line";
		int IndexInZeile=0;
		int tabs=0;

		
		while(IndexInZeile<LineFromFile.length()){
		if( LineFromFile.charAt(IndexInZeile)=='	' ){tabs++;};
		if( tabs==2 ){Annotation=LineFromFile.substring(IndexInZeile+1, LineFromFile.length());
					if (Annotation.charAt(1)=='-'){Annotation=Annotation.substring(2, Annotation.length());};
						tabs=0;
						System.out.println(Annotation);
		}
		IndexInZeile++;}
		return Annotation;
			}



}
