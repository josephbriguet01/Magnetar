/**
 * Copyright (c) 2007, 2016, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * Copyright 1999-2002,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tcp;



/**
 * Cette classe fournit une fonction de codage/décodage tel que définit par la RFC 2045, N. Freed and N. Borenstein&#46;
 * @author Jeffrey Rodriguez
 * @author Sandy Gao
 * @version 1.0
 */
public final class  Base64 {

    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS STATICS">
    static private final int  BASELENGTH         = 128;
    static private final int  LOOKUPLENGTH       = 64;
    static private final int  TWENTYFOURBITGROUP = 24;
    static private final int  EIGHTBIT           = 8;
    static private final int  SIXTEENBIT         = 16;
    static private final int  FOURBYTE           = 4;
    static private final int  SIGN               = -128;
    static private final char PAD                = '=';
    static private final boolean FDEBUG          = false;
    static final private byte [] BASE64ALPHABET        = new byte[BASELENGTH];
    static final private char [] LOOKUPBASE64ALPHABET  = new char[LOOKUPLENGTH];

    static {
        for (int i = 0; i < BASELENGTH; ++i)        BASE64ALPHABET[i]       = -1;
        for (int i = 'Z'; i >= 'A'; i--)            BASE64ALPHABET[i]       = (byte) (i-'A');
        for (int i = 'z'; i>= 'a'; i--)             BASE64ALPHABET[i]       = (byte) ( i-'a' + 26);
        for (int i = '9'; i >= '0'; i--)            BASE64ALPHABET[i]       = (byte) (i-'0' + 52);
        for (int i = 0; i<=25; i++)                 LOOKUPBASE64ALPHABET[i] = (char)('A'+i);
        for (int i = 26,  j = 0; i<=51; i++, j++)   LOOKUPBASE64ALPHABET[i] = (char)('a'+ j);
        for (int i = 52,  j = 0; i<=61; i++, j++)   LOOKUPBASE64ALPHABET[i] = (char)('0' + j);
        
        BASE64ALPHABET['+']         = 62;
        BASE64ALPHABET['/']         = 63;
        LOOKUPBASE64ALPHABET[62]    = (char)'+';
        LOOKUPBASE64ALPHABET[63]    = (char)'/';
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Correspond au constructeur par défaut
     */
    public Base64() {
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PRIVATES">
    /**
     * Vérifie si l'octet est un espace
     */
    private static boolean isWhiteSpace(char octect) {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }

    /**
     * Vérifie si l'octet est une touche directionnelle
     */
    private static boolean isPad(char octect) {
        return (octect == PAD);
    }

    /**
     * Vérifie si l'octet est une donnée
     */
    private static boolean isData(char octect) {
        return (octect < BASELENGTH && BASE64ALPHABET[octect] != -1);
    }

    /**
     * Vérifie si l'octet est un espace ou une touche directionnelle ou un donnée
     */
    private static boolean isBase64(char octect) {
        return (isWhiteSpace(octect) || isPad(octect) || isData(octect));
    }
    
    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param data  the byte array of base64 data (with WS)
     * @return      the new length
     */
    private static int removeWhiteSpace(char[] data) {
        if (data == null) return 0;

        // count characters that's not whitespace
        int newSize = 0;
        for (int i = 0; i < data.length; i++)  if (!isWhiteSpace(data[i])) data[newSize++] = data[i];
        
        return newSize;
    }
    // </editor-fold>

    

    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Encode les octets hexadécimaux en Base64
     * @param binaryData Correspond au tableau de données binaires
     * @return Retourne une chaine
     */
    public static String encode(byte[] binaryData) {

        if (binaryData == null)
            return null;

        int      lengthDataBits    = binaryData.length*EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int      fewerThan24bits   = lengthDataBits%TWENTYFOURBITGROUP;
        int      numberTriplets    = lengthDataBits/TWENTYFOURBITGROUP;
        int      numberQuartet     = fewerThan24bits != 0 ? numberTriplets+1 : numberTriplets;
        @SuppressWarnings("UnusedAssignment")
        char     encodedData[]     = null;

        encodedData = new char[numberQuartet*4];
        @SuppressWarnings("UnusedAssignment")
        byte k = 0;
        
        @SuppressWarnings("UnusedAssignment")
        byte l=0, b1=0,b2=0,b3=0;

        int encodedIndex = 0;
        int dataIndex   = 0;
        if (FDEBUG) {
            System.out.println("number of triplets = " + numberTriplets );
        }

        for (int i=0; i<numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            if (FDEBUG) {
                System.out.println( "b1= " + b1 +", b2= " + b2 + ", b3= " + b3 );
            }

            l  = (byte)(b2 & 0x0f);
            k  = (byte)(b1 & 0x03);

            byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);

            byte val2 = ((b2 & SIGN)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);
            byte val3 = ((b3 & SIGN)==0)?(byte)(b3>>6):(byte)((b3)>>6^0xfc);

            if (FDEBUG) {
                System.out.println( "val2 = " + val2 );
                System.out.println( "k4   = " + (k<<4));
                System.out.println( "vak  = " + (val2 | (k<<4)));
            }

            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[ val1 ];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[ val2 | ( k<<4 )];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[ (l <<2 ) | val3 ];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[ b3 & 0x3f ];
        }

        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) ( b1 &0x03 );
            if (FDEBUG) {
                System.out.println("b1=" + b1);
                System.out.println("b1<<2 = " + (b1>>2) );
            }
            byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[ val1 ];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[ k<<4 ];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex +1 ];
            l = ( byte ) ( b2 &0x0f );
            k = ( byte ) ( b1 &0x03 );

            byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
            byte val2 = ((b2 & SIGN)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);

            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[ val1 ];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[ val2 | ( k<<4 )];
            encodedData[encodedIndex++] = LOOKUPBASE64ALPHABET[ l<<2 ];
            encodedData[encodedIndex++] = PAD;
        }

        return new String(encodedData);
    }

    /**
     * Décode la chaine Base64 en octets hexadécimaux
     * @param encoded Correspond à la chaîne à décoder
     * @return Retourne un tableau de byte
     */
    public static byte[] decode(String encoded) {

        if (encoded == null)
            return null;

        char[] base64Data = encoded.toCharArray();
        // remove white spaces
        int len = removeWhiteSpace(base64Data);

        if (len%FOURBYTE != 0) {
            return null;//should be divisible by four
        }

        int      numberQuadruple    = (len/FOURBYTE );

        if (numberQuadruple == 0)
            return new byte[0];

        @SuppressWarnings("UnusedAssignment")
        byte     decodedData[]      = null;
        
        @SuppressWarnings("UnusedAssignment")
        byte     b1=0,b2=0,b3=0,b4=0;
        
        @SuppressWarnings("UnusedAssignment")
        char     d1=0,d2=0,d3=0,d4=0;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex    = 0;
        decodedData      = new byte[ (numberQuadruple)*3];

        for (; i<numberQuadruple-1; i++) {

            if (!isData( (d1 = base64Data[dataIndex++]) )||
                !isData( (d2 = base64Data[dataIndex++]) )||
                !isData( (d3 = base64Data[dataIndex++]) )||
                !isData( (d4 = base64Data[dataIndex++]) ))
                return null;//if found "no data" just return null

            b1 = BASE64ALPHABET[d1];
            b2 = BASE64ALPHABET[d2];
            b3 = BASE64ALPHABET[d3];
            b4 = BASE64ALPHABET[d4];

            decodedData[encodedIndex++] = (byte)(  b1 <<2 | b2>>4 ) ;
            decodedData[encodedIndex++] = (byte)(((b2 & 0xf)<<4 ) |( (b3>>2) & 0xf) );
            decodedData[encodedIndex++] = (byte)( b3<<6 | b4 );
        }

        if (!isData( (d1 = base64Data[dataIndex++]) ) ||
            !isData( (d2 = base64Data[dataIndex++]) )) {
            return null;//if found "no data" just return null
        }

        b1 = BASE64ALPHABET[d1];
        b2 = BASE64ALPHABET[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (!isData( (d3 ) ) ||
            !isData( (d4 ) )) {//Check if they are PAD characters
            if (isPad( d3 ) && isPad( d4)) {               //Two PAD e.g. 3c[Pad][Pad]
                if ((b2 & 0xf) != 0)//last 4 bits should be zero
                    return null;
                byte[] tmp = new byte[ i*3 + 1 ];
                System.arraycopy( decodedData, 0, tmp, 0, i*3 );
                tmp[encodedIndex]   = (byte)(  b1 <<2 | b2>>4 ) ;
                return tmp;
            } else if (!isPad( d3) && isPad(d4)) {               //One PAD  e.g. 3cQ[Pad]
                b3 = BASE64ALPHABET[ d3 ];
                if ((b3 & 0x3 ) != 0)//last 2 bits should be zero
                    return null;
                byte[] tmp = new byte[ i*3 + 2 ];
                System.arraycopy( decodedData, 0, tmp, 0, i*3 );
                tmp[encodedIndex++] = (byte)(  b1 <<2 | b2>>4 );
                tmp[encodedIndex]   = (byte)(((b2 & 0xf)<<4 ) |( (b3>>2) & 0xf) );
                return tmp;
            } else {
                return null;//an error  like "3c[Pad]r", "3cdX", "3cXd", "3cXX" where X is non data
            }
        } else { //No PAD e.g 3cQl
            b3 = BASE64ALPHABET[ d3 ];
            b4 = BASE64ALPHABET[ d4 ];
            decodedData[encodedIndex++] = (byte)(  b1 <<2 | b2>>4 ) ;
            decodedData[encodedIndex++] = (byte)(((b2 & 0xf)<<4 ) |( (b3>>2) & 0xf) );
            decodedData[encodedIndex++] = (byte)( b3<<6 | b4 );

        }

        return decodedData;
    }
    // </editor-fold>



}