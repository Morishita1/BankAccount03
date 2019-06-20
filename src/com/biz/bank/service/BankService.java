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

public interface BankService {

	/*
	 * interface
	 * 1. 앞으로 만들(생성할)클래스의 설계도이다.
	 * 2. 인터페이스 설계도를 기준으로 작성된 클래스들은
	 * 		어떤 곳에서 사용하던지 대체로 공통된 코드로
	 * 		내용을 작성할수 있다.
	 * 3. 인터페이스로 선언된 객체는 인터페이스를 implement한
	 * 		클래스로 초기화(생성)을 해서 사용을 한다.
	 */
	
	public void readBalance() throws IOException;
	public void writeBalance() throws IOException ;
	public BankBalanceVO pickAcc(String accNum) ;
	public void inputMoney(String acc,int money) ;
	public void outputMoney(String acc,int money) ;
	public int selectMenu() ;
}
