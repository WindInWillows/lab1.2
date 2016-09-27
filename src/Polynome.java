import java.util.ArrayList;
import java.util.Scanner;

// ʵ����һ���������ʽ����

/* 
 * ����
 *  1������һ������ʽ���ʽ��������
 *  2�����뻯������Ա��ʽ���л�������������
 *  3������������Ա��ʽ�����󵼲����������
 *  4�������˳�����˳�ϵͳ
 */

/*
 *  ������Ϣ
 *   1��Invalid character inside	��������� ���֡���ĸ���ո�tab��+��-��*��^ ���� ���ַ��ı��ʽ
 *   2��Wrong command			�����ʽ�����ڣ�ǰ�г��˿ո���Ʊ��֮����ַ�
 *   3��Unrecognized command		������δ֪������
 *   4��^ error. It should be var^num	��^��ʹ�ã�������var^numģʽ
 *   5��Polynome hasn't been entered 	������ʽǰ�����뻯����󵼵�����
 */

/*
 * ����ı��ʽ˵��
 *  1��ֻ�ܰ��� ���֡���ĸ���ո�tab��+��-��*��^ ���ַ�
 *  2�����԰����ո���Ʊ��
 *  3��֧�ּӼ���
 *  4��������ʹ�ñ���^���֣�������ʾָ������֧�����ֺ��^��^�����ĸ
 *  5���������ƿ�������ĸ�����ֵ���ϣ�����������
 *  6����֧������
 *  7����֧��*��ʡ��
 *  8����֧��ͬ����ϲ�
 */

/*
 * ����˵��
 *  1��֧�ֶ��������
 *  2��֧�ֻ���ı�����ֵΪ����
 *  3����֧��С��
 *  4�����������г���δ�ڱ��ʽ�г��ֵı��������
 *  5�����������г���������!simplify x y=1,�����x
 *  6�����������г��ִ���ı����ʽ�� x=x x=pi x=1.5,�������һ��
 *  7���������г���δ�ڱ��ʽ�г��ֵı����򷵻�0
 *  8����֧��ͬ����ϲ�
 */
public class Polynome {

	//--------------------------------------------------------
	// һ���������������ڽ��ܱ��ʽ���ַ���������
	private Scanner scan;
	// ͨ��ʹ��һ������ΪItem���б��浱ǰ���ʽ������Ҫ��
	private ArrayList<Item> expressionArray = new ArrayList<Item>();
	// ��ʱ�ַ��������ڱ���ȥ���ո���Ʊ��֮��������ַ���
	private String tmpStr = "";
	
	// opCodeΪ�����룬opStrΪ�����֡�������������getInput���޸ģ�������impOperation��
	// opCode������impOperation��ִ��ʲô���͵Ĳ���
	// opStr����opCode��ʵ��
	//	ִ�л������!simplify x=1 y=2��ʱ��opStr�б��� x=1 y=2;
	//	ִ��������(!d/d x)ʱ��opStr�б���x;
	//	���������Ϣ�ǣ�opStr���������Ϣ��
	private int opCode = 0;
	private String opStr = "";
	// �ַ�������
	private static final String EXIT_FLAG = "exit";
	private static final String SIMPLIFY = "simplify";
	private static final String DIFF = "d/d";
	//--------------------------------------------------------
	
	public Polynome() {
		//??
		scan = new Scanner(System.in);
	}

	public static void main(String[] args) {
		Polynome po = new Polynome();
		// ��ӡ��ʾ��Ϣ
		po.prompt();
		while(true){
			//��ȡһ���ַ������ж��Ǳ��ʽ�������������Ӧ�Ĵ����Լ��õ�һ��������
			po.getInput();
			//������һ���л�õĲ�����ִ����Ӧ�Ĳ���
			po.impOperation();
		}
	}
	
	public void prompt(){
		// ϵͳ��ʼ���к��ӡ��ʾ��Ϣ
		// ���ڴ���Ӱ����汾�������ȵ���ʾ��Ϣ
		System.out.println("<Welcome to Polynomials System 1.0>");
	}
	
	
	public void getInput(){
		
		// ��ȡһ���ַ���
		System.out.print(">> ");
		String strInput = scan.nextLine();
		
		// �������ʱ������������
		if (strInput.isEmpty()){
			opCode = 0;
			return;
		}
		
		// ���ַ����в���������������Ϊ���ʽ������Ϊ���
		// �����н�����Ӧ�Ĵ�����
		if (strInput.indexOf("!")==-1) expression(strInput);
		else command(strInput);
  	}
	
	public void impOperation() {
		
		// ���ݲ�����ִ����Ӧ����	
		switch(opCode) {
			case -1: exitSys(); break; 	// �˳�ϵͳ
			case 0:				break; 	// �ղ���,Ĭ��ֵ
			case 1:	print();    break;	// ��ӡ��ǰ���ʽ
			case 2:	simplify(); break;	// ������ʽ
			case 3:	derivative();break; // �Ա��ʽ��
			case 4:	errorOutput();break;// ���������Ϣ��������Ϣ������opStr��
			default: 	        break;  // �ղ���
		}
	}
	
	
	private void expression(String strInput){
		
		// ������ StrInput �ĺϷ��Խ����ж���ͬʱȥ�����ʽ�еĿո���Ʊ��
		if (validateExpressionAndStrip(strInput) == false){
			opCode = 4;
			opStr  = "Invalid character inside";
			return;
		}
		
		// ����+��-���Ա��ʽ��Ӧ���ַ������в��
		// ���Բ�ֺ�ĸ����ַ�����Ϊ�������ֱ���Item���󣬼��뵽expressionArray��
		buildItem();
	}
	
	
	private boolean validateExpressionAndStrip(String str){
		// �� tmpStr ���г�ʼ��
		tmpStr = "";
		
		char[] chars = str.toCharArray();
		for(char ch : chars){
			// ������ ���֡���ĸ���ո�tab��+��-��*��^ ���� ���ַ�
			if(ch != ' ' && ch != '\t' ){
				if(!((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') 
				|| (ch >= 'A' && ch <= 'Z') || ch == '+' || ch == '-'
				||  ch == '*' || ch == '^')){
					return false;
				}
				tmpStr += ch;
			}
		}
		return true;
	}
	
	private void buildItem(){
		// �Դ洢���ʽ���б���г�ʼ��
		expressionArray.clear();
		
		// ��һ�����ʽ�ַ�������+��-,���в��
		// ��һ��ķ���Ĭ��Ϊ��
		String itemStr = "+";
		char[] chars = tmpStr.toCharArray();
		for (char ch : chars){
			if (ch == '+'){
				if (addNewItem(itemStr) == false) return;
				// ��һ��ķ���Ϊ��
				itemStr = "+";
			}
			else if (ch == '-'){
				if (addNewItem(itemStr) == false) return;
				// ��һ��ķ���Ϊ��
				itemStr = "-";
			}
			else itemStr += ch;
		}
		if (addNewItem(itemStr) == false) return;
		// ��opCode��ֵ��Ϊ1,��ʾ������ʽ�ɹ�����impOperation��,�Ա��ʽ���д�ӡ
		else opCode = 1;
	}
	
	
	private boolean addNewItem(String itemStr){
		Item newItem = (new Item(itemStr));
		// ����itemStrʱ������
		if(newItem.errorFlag == true) {
			opCode = 4;
			opStr = "^ error. It should be var^num";
			return false;
		}
		expressionArray.add(newItem);
		return true;
	}
	
	private void command(String strInput){
		// ������������ĺϷ���
		if (validateCommandAndStrip(strInput) == false)
		{
			opCode = 4;
			opStr = "Wrong command";
			return;
		}
		
		// �ж���������,���޸�opCode��opStr
		getCommandType();
	}
	
	private boolean validateCommandAndStrip(String str){
		// �������������Ƿ��ڣ�ǰ�г��ո���Ʊ��֮����ַ�
		// ����У���˵���������������
		char[] chars = str.toCharArray();
		boolean checkFlag = true;
		for (char ch : chars){
			if(ch == '!'){
				checkFlag = false;
				break;
			}
			else if((ch != ' ' && ch != '\t') && checkFlag){
				return false;
			}
		}
		// !!! ����ӹ��� ȥ���ո�
		tmpStr = str;
		return true;
	}

	
	private void getCommandType(){

		int index;
		if ((index = tmpStr.indexOf(SIMPLIFY)) != -1) {
			index += SIMPLIFY.length() + 1;
			opCode = 2;//������ʽ
		}
		else if((index = tmpStr.indexOf(DIFF)) != -1) {
			index += DIFF.length() + 1;
			opCode = 3;//���ʽ��
		}
		else if((tmpStr.indexOf(EXIT_FLAG) != -1)){
			opCode = -1;//�˳�����
			return;
		}
		else {
			opCode = 4;//�������
			opStr = "Unrecognized command";
			return;
		}
		if (!this.expressionArray.isEmpty()) opStr = tmpStr.substring(index);
		else{
			opCode = 4;
			opStr="Polynome hasn't been entered";
		}
	}
	
	private void simplify() {
		String resStr = "";
		boolean firstFlag = true;//Ҳ�����Ż�
		
		for (int i=0; i<this.expressionArray.size();i++){
			resStr += this.expressionArray.get(i).simplify(opStr).toString(firstFlag);
			firstFlag = false;
		}
		System.out.println(resStr);
	}

	private void derivative() {
		String resStr = "";
		boolean firstFlag = true;//Ҳ�����Ż�
		for (int i=0; i<this.expressionArray.size();i++){
			Item diffItem = this.expressionArray.get(i).diff(this.opStr);
			if (diffItem == null) resStr += "";
			else {
				resStr += diffItem.toString(firstFlag);
				firstFlag = false;
			}
		}
		if (resStr.isEmpty()) resStr = "";
		System.out.println(resStr);
	}
	

	private void exitSys() {
		System.out.println("Thanks for use!");
		System.exit(0);
	}
	
	private void errorOutput(){
		System.out.println("Error: "+opStr+".");
	}
	
	private void print() {
		String resStr = "";
		boolean firstFlag = true;//Ҳ�����Ż�
		for (int i=0; i<this.expressionArray.size();i++){
			resStr+=this.expressionArray.get(i).toString(firstFlag);
			firstFlag = false;
		}
		System.out.println(resStr);
	}
	
}

