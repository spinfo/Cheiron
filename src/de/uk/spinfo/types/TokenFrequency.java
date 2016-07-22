

/* First created by JCasGen Fri Jul 22 13:16:57 CEST 2016 */
package de.uk.spinfo.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import de.julielab.jcore.types.Token;


/** 
 * Updated by JCasGen Fri Jul 22 13:16:57 CEST 2016
 * XML source: /home/phil/Documents/Spinfo/Prometheus/Cheiron/desc/spinfo-frequency-types.xml
 * @generated */
public class TokenFrequency extends DocumentAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TokenFrequency.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TokenFrequency() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public TokenFrequency(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public TokenFrequency(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public TokenFrequency(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: token

  /** getter for token - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getToken() {
    if (TokenFrequency_Type.featOkTst && ((TokenFrequency_Type)jcasType).casFeat_token == null)
      jcasType.jcas.throwFeatMissing("token", "de.uk.spinfo.types.TokenFrequency");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TokenFrequency_Type)jcasType).casFeatCode_token)));}
    
  /** setter for token - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setToken(FSArray v) {
    if (TokenFrequency_Type.featOkTst && ((TokenFrequency_Type)jcasType).casFeat_token == null)
      jcasType.jcas.throwFeatMissing("token", "de.uk.spinfo.types.TokenFrequency");
    jcasType.ll_cas.ll_setRefValue(addr, ((TokenFrequency_Type)jcasType).casFeatCode_token, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for token - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public Token getToken(int i) {
    if (TokenFrequency_Type.featOkTst && ((TokenFrequency_Type)jcasType).casFeat_token == null)
      jcasType.jcas.throwFeatMissing("token", "de.uk.spinfo.types.TokenFrequency");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TokenFrequency_Type)jcasType).casFeatCode_token), i);
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TokenFrequency_Type)jcasType).casFeatCode_token), i)));}

  /** indexed setter for token - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setToken(int i, Token v) { 
    if (TokenFrequency_Type.featOkTst && ((TokenFrequency_Type)jcasType).casFeat_token == null)
      jcasType.jcas.throwFeatMissing("token", "de.uk.spinfo.types.TokenFrequency");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TokenFrequency_Type)jcasType).casFeatCode_token), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TokenFrequency_Type)jcasType).casFeatCode_token), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: frequency

  /** getter for frequency - gets 
   * @generated
   * @return value of the feature 
   */
  public int getFrequency() {
    if (TokenFrequency_Type.featOkTst && ((TokenFrequency_Type)jcasType).casFeat_frequency == null)
      jcasType.jcas.throwFeatMissing("frequency", "de.uk.spinfo.types.TokenFrequency");
    return jcasType.ll_cas.ll_getIntValue(addr, ((TokenFrequency_Type)jcasType).casFeatCode_frequency);}
    
  /** setter for frequency - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setFrequency(int v) {
    if (TokenFrequency_Type.featOkTst && ((TokenFrequency_Type)jcasType).casFeat_frequency == null)
      jcasType.jcas.throwFeatMissing("frequency", "de.uk.spinfo.types.TokenFrequency");
    jcasType.ll_cas.ll_setIntValue(addr, ((TokenFrequency_Type)jcasType).casFeatCode_frequency, v);}    
   
    
  //*--------------*
  //* Feature: value

  /** getter for value - gets 
   * @generated
   * @return value of the feature 
   */
  public String getValue() {
    if (TokenFrequency_Type.featOkTst && ((TokenFrequency_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "de.uk.spinfo.types.TokenFrequency");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TokenFrequency_Type)jcasType).casFeatCode_value);}
    
  /** setter for value - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setValue(String v) {
    if (TokenFrequency_Type.featOkTst && ((TokenFrequency_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "de.uk.spinfo.types.TokenFrequency");
    jcasType.ll_cas.ll_setStringValue(addr, ((TokenFrequency_Type)jcasType).casFeatCode_value, v);}    
  }

    