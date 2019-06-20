package com.biz.bank.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.biz.bank.model.BankBalanceVO;

public class BankServiceimp_01 implements BankService {

	String accIolistPath = null ;
	String balanceFile=null;
	List<BankBalanceVO> balanList= null;
	FileReader fileReader= null;
	BufferedReader buffer= null;
	Scanner scan= null;

	
	public BankServiceimp_01(String balanceFile) throws IOException {
		
		/*
		 * balanceFile 이름을 필드에 있는
		 * this.balanceFile 에 저장하여
		 * 클래스 내에서 자유롭게 접근할수 있도록 한다.
		 */
		this.balanceFile= balanceFile;
		
		// iolist를 저장할 폴더 선언
		accIolistPath ="src/com/biz/bank/iolist/";
		balanList=new ArrayList<BankBalanceVO>();
		fileReader=new FileReader(balanceFile);
		buffer=new BufferedReader(fileReader);
		scan=new Scanner(System.in);
	}
	
	/*
	 * balance.txt 파일을 읽어서
	 * 계좌정보를 balanceList에 추가하는 메서드를 작성
	 */

	public void readBalance() throws IOException {
	
		String reader="";
		
		while(true) {
			reader=buffer.readLine();
			if(reader==null)break;
			String[] readers=reader.split(":");
			BankBalanceVO vo=new BankBalanceVO(readers[0],Integer.valueOf(readers[1]),readers[2]);
			
			balanList.add(vo);
		}
		// balance.txt 파일을 처음한번 읽어서
		// balanceList에 담고 나면
		// buffer와 fileReader는 하는일 끝나므로
		// 두 객체 모두 close() 실행하자
		// ※ 프로젝트가 종료될때
		// balance.txt 파일에 내용을 기록해야하는데
		// reader 상태로 열려 있으면
		// 기록이 잘 안되는 경우가 있기 때문이다.
		buffer.close();
		fileReader.close();
		
	}// readBalance end
	
	/*
	 * 이 메서드는 프로젝트가 종료되기 직전에 실행되어서
	 * balanceLiust에 담긴 내용을 balance.txt에 전부 기록한다.
	 */
	public void writeBalance() throws IOException {
		
		FileWriter fileWriter;
		PrintWriter printWriter;
		
		fileWriter= new FileWriter(balanceFile);
		printWriter=new PrintWriter(fileWriter);
		
		for(BankBalanceVO vo: balanList) {
			printWriter.printf("%s:%d:%s\n", vo.getAcc(),vo.getBalance(),vo.getDate());
		}
		printWriter.flush();
		printWriter.close();
	}
	
	
	public BankBalanceVO pickAcc(String accNum) {
		//String acc="0001";
		/*
		 * balanceList 에서 계좌번호 0001인 데이터를 찾고
		 * 그 계좌의 현잔액을 console에 표시
		 */
		
		int intLen=balanList.size();
		int index=0;
		BankBalanceVO vo=null;
		for(index=0 ;index<intLen;index++){
				vo=balanList.get(index);
				//System.out.println(vo.getBalance());
			if(vo.getAcc().equals(accNum)) {
				return vo;
			//break;
		}
		
		}
		return null;
	}
	
	/*
	 * 계좌번호로 pickAcc()로부터 vo 값을 추출하고
	 * balance값과 money 값을 더하여
	 * vo의 balance에 저장하고
	 * 콘솔에 보여주는 코드
	 */
	public void inputMoney(String acc,int money) {
		/*
		 * 만약 acc(계좌번호)가 없는 데이터를
		 * pickAcc()메서드에게 보내면
		 * pickAcc()메서드는 null값을 return 할 것이다.
		 * 
		 * 일헌 상황이되면
		 * 이후 코드에서 NullpointException이 발생을 한다.
		 */
		BankBalanceVO vo=pickAcc(acc);
		/*
		 * vo 값이 null인경우는 다음코드로 진행하지 못하도록하며
		 * 그 전에 사용자에게 메시지를 보여주어야 한다.
		 */
		
		vo.setBalance(vo.getBalance()+money);
		//System.out.println(vo.getBalance());
		// java 1.7 이하에서 지금도 사용하는 코드
		// 현재 컴퓨터날짜값을 가져오기
		Date date=new Date(System.currentTimeMillis());
		
		SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
		String curDate=sf.format(date);
		vo.setDate(curDate);
	
		// java 1.8(8) 이상에서 사용하는 새로운 날짜
		LocalDate localDate=LocalDate.now();
		vo.setDate(localDate.toString());
		
		// 입금이 잘 되었나를 콘솔에 확인 시켜주는 부분
		System.out.println("==============================");
		System.out.println(vo);
		System.out.println("==============================");
		
		FileWriter fileWriter;
		PrintWriter printWriter;
		
		// 계좌번호를 임시변수에 대입(저장)
		String accNum= vo.getAcc();
		
		/*
		 * 입출금 거래내역을 개인통장에 기록
		 */
		try {
			/*
			 * create mode :
			 * fileWriter로 파일을 기록하기 위해서 실행하면
			 * 기존에 같은 이름의 파일이 있으면
			 * 삭제하고 새로 생성을 한다
			 * 
			 * append mode :
			 * 만약 기존에 파일 내용을 유지하면서
			 * 파일의 끝에 새로운 내용을 추가로 저장하고 싶으면
			 * new FileWriter() 생성자 끝에 true 옵션을 추가한다.
			 */
			fileWriter=new FileWriter(accIolistPath+"KBANK"+accNum,true);
			printWriter= new PrintWriter(fileWriter);
			
			// 파일에 내용을 기록하는 부분
			printWriter.printf("%s:%s:%d:%d:%d\n", vo.getDate(),"입금",money,0,vo.getBalance());
			
			printWriter.flush();
			printWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void outputMoney(String acc,int money) {
		BankBalanceVO vo=pickAcc(acc);
		
		int bal=vo.getBalance();
		// 출금일경우는 현잔액을 검사해서
		// 출금액보다 크면 출금 금지
		if(bal<money) {
			System.out.println("잔액부족!!!");
			return;
		}
		vo.setBalance(bal-money);
		//System.out.println(vo.getBalance());
		// java 1.7 이하에서 지금도 사용하는 코드
		// 현재 컴퓨터날짜값을 가져오기
		Date date=new Date(System.currentTimeMillis());
		
		SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
		String curDate=sf.format(date);
		vo.setDate(curDate);
	
		// java 1.8(8) 이상에서 사용하는 새로운 날짜
		LocalDate localDate=LocalDate.now();
		vo.setDate(localDate.toString());
		System.out.println("==============================");
		System.out.println(vo);
		System.out.println("==============================");
		
		FileWriter fileWriter;
		PrintWriter printWriter;
		
		// 계좌번호를 임시변수에 대입(저장)
		String accNum= vo.getAcc();
		
		/*
		 * 출금 거래내역을 개인통장에 기록
		 */
		try {
			fileWriter=new FileWriter(accIolistPath+"KBANK"+accNum,true);
			printWriter= new PrintWriter(fileWriter);
			
			// 파일에 내용을 기록하는 부분
			printWriter.printf("%s:%s:%d:%d:%d\n", vo.getDate(),"출금",0,money,vo.getBalance());
			
			printWriter.flush();
			printWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}
	public int selectMenu() {
		System.out.println("=============================");
		System.out.println("1. 입금   2. 출금    -9.종료");
		System.out.println("-----------------------------");
		System.out.print("업무선택>>");
		String strMenu=scan.nextLine();
		
		int intMenu=0;
		try {
			intMenu=Integer.valueOf(strMenu);
		} catch (Exception e) {
			// 오류 무시
		}
		return intMenu;
	}
}
