/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, 10/2022
 */
package com.jasonpercus.magnetar.protocol;



/**
 * Cette classe représente un protocol réseau
 * @author JasonPercus
 * @version 1.0
 */
public abstract class Protocol implements java.io.Serializable, Comparable<Protocol>, Cloneable, CharSequence {
    
    
    
//METHODES PUBLICS
    /**
     * Renvoie le nom du protocol réseau
     * @return Retourne le nom du protocol réseau
     */
    public abstract String getProtocolName();

    /**
     * Returns a stream of code point values from this sequence.  Any surrogate
     * pairs encountered in the sequence are combined as if by {@linkplain
     * Character#toCodePoint Character.toCodePoint} and the result is passed
     * to the stream. Any other code units, including ordinary BMP characters,
     * unpaired surrogates, and undefined code units, are zero-extended to
     * {@code int} values which are then passed to the stream.
     *
     * <p>If the sequence is mutated while the stream is being read, the result
     * is undefined.
     *
     * @return an IntStream of Unicode code points from this sequence
     * @since 1.8
     */
    @Override
    public java.util.stream.IntStream codePoints() {
        return getProtocolName().codePoints();
    }

    /**
     * Returns a stream of {@code int} zero-extending the {@code char} values
     * from this sequence.  Any char which maps to a <a
     * href="{@docRoot}/java/lang/Character.html#unicode">surrogate code
     * point</a> is passed through uninterpreted.
     *
     * <p>If the sequence is mutated while the stream is being read, the
     * result is undefined.
     *
     * @return an IntStream of char values from this sequence
     * @since 1.8
     */
    @Override
    public java.util.stream.IntStream chars() {
        return getProtocolName().chars();
    }

    /**
     * Renvoie une séquence de caractères qui est une sous-séquence de cette séquence. La sous-séquence commence par le caractère à l'index spécifié et se termine par le caractère à l'index end-1 . La longueur (en chars) de la séquence renvoyée est end - start , donc si start == end alors une séquence vide est renvoyée
     * @param start Correspond à l'indice de départ, inclus
     * @param end Correspond à l'indice de fin, exclusif
     * @return Retourne la sous-séquence spécifiée
     *
     * @throws  IndexOutOfBoundsException Si start ou end sont négatifs, si end est supérieur à {@link #length()} , ou si start est supérieur à end
     */
    @Override
    public CharSequence subSequence(int start, int end){
        return getProtocolName().subSequence(start, end);
    }

    /**
     * Renvoie le caractère à l'index spécifié. Un index va de zéro à length() - 1 . Le premier caractère de la séquence est à l'index zéro, le suivant à l'index un, et ainsi de suite, comme pour l'indexation des tableaux
     * @param index Correspond à l'indice du caractère à retourner
     * @return Retourne le caractère spécifié
     * @throws IndexOutOfBoundsException Si l'argument d'index est négatif ou supérieur ou égal à {@link #length()}
     */
    @Override
    public char charAt(int index){
        return getProtocolName().charAt(index);
    }

    /**
     * Renvoie la longueur de cette séquence de caractères
     * @return Retourne le nombre de chars dans cette séquence
     */
    @Override
    public int length(){
        return getProtocolName().length();
    }

    /**
     * Renvoie le hashCode du protocole
     * @return Retourne le hashCode du protocole
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + java.util.Objects.hashCode(this.getProtocolName());
        return hash;
    }

    /**
     * Détermine si deux protocoles sont identiques
     * @param obj Correspond au second protocole à comparer au courant
     * @return Retourne true s'ils sont identiques, sinon false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Protocol other = (Protocol) obj;
        return java.util.Objects.equals(this.getProtocolName(), other.getProtocolName());
    }

    /**
     * Renvoie le protocole sous la forme d'une chaîne de caractères
     * @return Retourne le protocole sous la forme d'une chaîne de caractères
     */
    @Override
    public String toString() {
        return getProtocolName() + " Protocol";
    }

    /**
     * Compare deux protocoles entre-eux
     * @param o Correspond au second protocole à comparer au courant
     * @return Retourne le résultat de la comparaison
     */
    @Override
    public int compareTo(Protocol o){
        return o == null ? -1 : this.getProtocolName().compareTo(o.getProtocolName());
    }
    
    
    
}