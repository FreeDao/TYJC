package com.kun.tyjc;

public class Command 
{		
	public static byte[] getByteFromInt(int[] i,int len) 
	{
        byte[] temp = new byte[len];
        
        for (int j = 0 ; j < len; j++)
        {
        	temp[j] = getByteFromInt(i[j]);
        }

		return  temp;
	}
	
	 public static byte[] getByteFromInt(int[] i,int start,int len) 
		{
	        byte[] temp = new byte[len];
	        
	        for (int j = 0 ; j < len; j++)
	        {
	        	temp[j] = getByteFromInt(i[start+j]);
	        }

			return  temp;
		}

	
	public static byte getByteFromInt(int i) 
	{

		int temp = i;

		if (temp >= 128) {
			temp -= 256;
		}

		return (byte) temp;

	}
	
	public static int getIntFromByte(byte b) {

		int temp = b;

		if (b < 0) {
			temp += 256;
		}

		return temp;
	}
	
	
}
