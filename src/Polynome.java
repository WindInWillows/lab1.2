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




class Item {
	public int coe=1;
	private ArrayList<Node> item = new ArrayList<Node>();
	public boolean errorFlag = false;
	/*
	 *������ʽ��������������������ʽ������ݽṹ��
	 *	���ʽ��ı���������һ��ArrayList���
	 *	���ʽ��ϵ����������
	 */
	//'-','11*x*y*x*8' => -88,Node(x,2),Node(y,1)
	public Item(String exp, boolean isPositive) {
		////����ϵ������������
		if( !isPositive){
			this.coe *= -1;
		}
		////���һ������ʽ��һ��
		//��'11*x*y*x*8'���Ϊ   �б�[11 x y x 8]
		String[] tmp_list=exp.split("\\*");
		for(int i=0;i<tmp_list.length;i++){
			String tmp_i=tmp_list[i];
			Integer num=isNumber(tmp_i);
			
			Integer power = 1;
			if(tmp_i.indexOf("^") != -1) {
				String[] tmmp = tmp_i.split("\\^");
				power=isNumber(tmmp[1]);
				if (power == null ){
					errorFlag = true;
					return;
				}
				if (isNumber(tmmp[0]) != null ){
					errorFlag = true;
					return;
				}
				tmp_i = tmmp[0];
			}
			
			// num=null ��ʾtmp_i�������֣����򷵻�������֣�int�������������ֱ����ϵ����˼���
			if( num!= null ){
				this.coe *= num;
			}
			// tmp_i Ϊ������������Ĵ������жϱ���tmp_i�Ƿ���ֹ�
			else{
				//�ж��ַ�tmp_i�Ƿ��Ѿ����ֵı�־
				boolean appear_flag=false;// ��Ҳ���ܱ��Ż�
				//����tmp_list���Ѿ�����Ĳ��֣��жϱ���tmp_i�Ƿ���ֹ�
				for(int j=0;j<this.item.size();j++){
					Node Node_j=this.item.get(j);
					// ����tmp_i������֮ǰ������ַ����У���ԭ������Ӧ�ڵ��ָ��+1
					if(Node_j.v.equals(tmp_i)){
						Node_j.inc(power);
						appear_flag=true;
					}
				}
				//���δ���ֵı���
				if (appear_flag==false){
					this.item.add(new Node(tmp_i, power));
				}
			}
		}
	}
	
	public Item() {}

	public Item(String itemStr) {
		this(itemStr.substring(1),itemStr.toCharArray()[0] == '+' );
	}

	public Item diff(String x) {
		Item tmp = getCopy();
		for(int i=0;i<tmp.item.size();i++){
			if (tmp.item.get(i).v.equals(x)){
				tmp.coe *= tmp.item.get(i).power;
				tmp.item.get(i).dec();
				return tmp;
			}
		}
		return null;
	}
	private Item getCopy(){
		// Ҳ�����Ż�
		Item res = new Item();
		res.coe = this.coe;
		for(int i=0;i<this.item.size();i++){
			res.item.add(this.item.get(i).getCopy());
		}
		return res;
	}
	
	//�����ǰ��
	public String toString(Boolean firstFlag) {
		String str = "";
		if (coe == 0) return "";
		if(coe > 0 && !firstFlag)
			str += "+";
		str += coe;
//		if(coe!=1) str += coe;
//		else if(item.isEmpty()) str+=coe;
		for(Node n : item) {
			if(n.power > 1 && !n.v.equals("")){
				if(str.length() > 0) str += "*";
				str += n.v+"^"+n.power;
			}
			else if(n.power == 1 && !n.v.equals("")){
				if(str.length() > 0) str += "*";
				str += n.v;
			}
		}
		return str;
	}
	
	//�ж������Ƿ��ܺϲ�
	private boolean equals(Item it) {
		ArrayList<Node> arr = it.item;
		for(Node n : arr){
			if(!this.hasNode(n))
				return false;
		}
		return true;
	}
	
	//�жϸ������Ƿ��д˱���
	private boolean hasNode(Node n) {
		for(Node nn:item){
			if(nn.equals(n))
				return true;
		}
		return false;
	}
	
	private boolean hasVarialbe(Node n) {
		for(Node nn:item){
			if(nn.v.equals(n.v))
				return true;
		}
		return false;
	}
	
	private Node hasVarialbe(String v) {
		for(Node nn:item){
			if(nn.v.equals(v))
				return nn;
		}
		return null;
	}	
	
	//Ϊ���ʽ��ֵ��֧�ֶ��������ֵ
	//����null˵���Ҳ���
	public Item simplify(String value) {
		Item tmp = getCopy();
		
		String[] strArray = {value};
		if (value.indexOf(" ") != -1){
			strArray = value.split(" ");
		}
		for (String tmpStr : strArray){
			if (tmpStr.indexOf("=") != -1){
				String[] ss = tmpStr.split("=");
				String name = ss[0];
				Integer v = isNumber(ss[1]);
				if (v == null) continue;
				Node n = null;
				if((n = tmp.hasVarialbe(name)) != null){
					tmp.coe *= (int)Math.pow(v, n.power);
					tmp.item.remove(n);
				}
			}
		}
		return tmp;
	}
	
	private Integer isNumber(String s) {
		Integer tmp = null;
		try{
			 tmp = Integer.parseInt(s);
		} catch(NumberFormatException e){
			return null;
		}
		return tmp;
	}
}

class Node {
	public Node(String v,int power) {
		this.power = power;
		this.v = v;
	}

	public Node() {
	}

	public int power;
	public String v;
	
	public Node(String v) {
		this.power = 1;
		this.v = v;
	}
	
	public void inc(int power) {
		this.power += power;
	}
	
	public void dec() {
		this.power--;
	}
	
	public Node getCopy(){
		Node res = new Node();
		res.power = this.power;
		res.v = this.v;
		return res;
	}
	
	public boolean equals(Node n) {
		return n.v.equals(this.v)
		&& n.power == this.power;
	}
}

