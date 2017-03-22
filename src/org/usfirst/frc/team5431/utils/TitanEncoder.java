package org.usfirst.frc.team5431.utils;

import edu.wpi.first.wpilibj.Encoder;

public class TitanEncoder extends Encoder {
	public TitanEncoder(int enc_id_one, int enc_id_two,boolean reverse,float diameter){
		super(enc_id_one, enc_id_two,reverse,EncodingType.k4X);
		this.setDistancePerPulse(diameter * Math.PI/360);
 		this.setSamplesToAverage(4);
	}
	
}