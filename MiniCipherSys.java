package mcs;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;


/**
 * This class implements a mini cipher system.
 *
 * @author RU NB CS112
 */
public class MiniCipherSys {

	/**
	 * Circular linked list that is the sequence of numbers for encryption
	 */
	SeqNode seqRear;

	/**
	 * Makes a randomized sequence of numbers for encryption. The sequence is 
	 * stored in a circular linked list, whose last node is pointed to by the field seqRear
	 */
	public void makeSeq() {
		// start with an array of 1..28 for easy randomizing
		int[] seqValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < seqValues.length; i++) {
			seqValues[i] = i+1;
		}

		// randomize the numbers
		Random randgen = new Random();
		for (int i = 0; i < seqValues.length; i++) {
			int other = randgen.nextInt(28);
			int temp = seqValues[i];
			seqValues[i] = seqValues[other];
			seqValues[other] = temp;
		}

		// create a circular linked list from this sequence and make seqRear point to its last node
		SeqNode sn = new SeqNode();
		sn.seqValue = seqValues[0];
		sn.next = sn;
		seqRear = sn;
		for (int i=1; i < seqValues.length; i++) {
			sn = new SeqNode();
			sn.seqValue = seqValues[i];
			sn.next = seqRear.next;
			seqRear.next = sn;
			seqRear = sn;
		}
	}

	/**
	 * Makes a circular linked list out of values read from scanner.
	 */
	public void makeSeq(Scanner scanner)
			throws IOException {
		SeqNode sn = null;
		if (scanner.hasNextInt()) {
			sn = new SeqNode();
			sn.seqValue = scanner.nextInt();
			sn.next = sn;
			seqRear = sn;
		}
		while (scanner.hasNextInt()) {
			sn = new SeqNode();
			sn.seqValue = scanner.nextInt();
			sn.next = seqRear.next;
			seqRear.next = sn;
			seqRear = sn;
		}
	}

	/**
	 * Implements Step 1 - Flag A - on the sequence.
	 */
	void flagA() {
		SeqNode ptr = seqRear.next;
		do {
			ptr=ptr.next;
		}
		while (ptr.next.seqValue!=27);
		if (ptr.next.seqValue == 27) {
			SeqNode A = ptr;
			SeqNode B= ptr.next;
			SeqNode C = ptr.next.next;
			SeqNode D = ptr.next.next.next;
			A.next = C;
			C.next = B;
			B.next = D;

		}
		// COMPLETE THIS METHOD
	}

	/**
	 * Implements Step 2 - Flag B - on the sequence.
	 */
	void flagB() {
		SeqNode ptr = seqRear.next;
		do {
			ptr = ptr.next;
		}
		while (ptr.next.seqValue!=28) ;
		if (ptr.next.seqValue ==28) {
			SeqNode A = ptr;
			SeqNode B= ptr.next;
			SeqNode C = ptr.next.next;
			SeqNode D = ptr.next.next.next;
			SeqNode last = D.next;
			A.next = C;
			C.next = D;
			D.next = B;
			B.next = last;

		}

		// COMPLETE THIS METHOD
	}

	/**
	 * Implements Step 3 - Triple Shift - on the sequence.
	 */
	void tripleShift() {
		SeqNode startNode = seqRear.next;
		SeqNode lastNode = seqRear;
		SeqNode sn = startNode;
		SeqNode prevToFirstFlag = null;
		SeqNode first_Flag = null;
		SeqNode second_Flag = null;
		SeqNode secondNext = null;

		do {
			if (	(sn.next.seqValue == 27 || sn.next.seqValue == 28) && prevToFirstFlag == null) {
				prevToFirstFlag = sn;
			}
			if ((sn.seqValue == 27 || sn.seqValue == 28) && first_Flag == null) {
				first_Flag = sn;
				sn = sn.next;
			}
			if ((sn.seqValue == 28 || sn.seqValue == 27) && second_Flag == null) {
				second_Flag = sn;
				secondNext = sn.next;
			}
			sn = sn.next;
		}
		while (sn != startNode);
		second_Flag.next= startNode;
		prevToFirstFlag.next = secondNext;
		lastNode.next = first_Flag;
		seqRear = prevToFirstFlag;
//	System.out.println("Triple Shift seqRear="+seqRear.seqValue);
	}
	// COMPLETE THIS METHOD
	/**
	 * Implements Step 4 - Count Shift - on the sequence.
	 */
	void countShift() {
		SeqNode rear = seqRear;
		SeqNode startNode = seqRear.next;
		SeqNode ptr = startNode;
		SeqNode lastNode = seqRear;
		SeqNode prevtoLastNode = null;
		SeqNode newStartNode = null;
		SeqNode endNode = null;
		int i = 0;
		int lastValue = seqRear.seqValue -1;
		if (lastValue == 28) {
			lastValue = 27;
		}
		do {
			if ((i == lastValue) && (endNode == null)) {
				endNode = ptr;
				newStartNode = endNode.next;
			}
			if (ptr.next == seqRear) {
				prevtoLastNode =ptr;
			}
			ptr = ptr.next;
			if (i != lastValue)
				i++;
		}
		while (ptr.next != startNode);
		lastNode.next=newStartNode;
		prevtoLastNode.next = startNode;
		endNode.next = lastNode;
//	seqRear = rear;
		// COMPLETE THIS METHOD
	}

	/**
	 * Gets a key. Calls the four steps - Flag A, Flag B, Triple Shift, Count Shift, then
	 * counts down based on the value of the first number and extracts the next number
	 * as key. But if that value is 27 or 28, repeats the whole process (Flag A through Count Shift)
	 * on the latest (current) sequence, until a value less than or equal to 26 is found,
	 * which is then returned.
	 *
	 * @return Key between 1 and 26
	 */
	int getKey() {
		flagA();
		flagB();
		tripleShift();
		countShift();
		SeqNode ptr = seqRear.next; //pointer is pointing to the first node
		SeqNode startNode = seqRear.next;
		int seqValue = startNode.seqValue;
//		System.out.println("getKey()seqValue="+seqValue);
		int key ;
		if (seqValue ==28) { //if start node is 28 makes the start node 27
			seqValue =27;
		}
		for (int j=0; j < seqValue-1 ; j++) {
			ptr = ptr.next;}

		key = ptr.next.seqValue;
		if (key== 27 || key ==28) { //if the key is not 27 or 28

			return getKey(); //give the value of the key which is ptr's next value
		}
		else {
			return key; //recursion
		}

		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE

	}

	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 *
	 * @param rear Rear pointer
	 */
	public void printList(SeqNode rear) {
		if (rear == null) {
			return;
		}
		System.out.print(rear.next.seqValue);
		SeqNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.seqValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 *
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {
		StringBuilder sb = new StringBuilder();
		int i =0;
		String alphabet ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String newMessage = ""; //initializes empty string
		System.out.println(newMessage);
		do {
			char c = message.charAt(i);
			int convertLetter = alphabet.indexOf(c) +1 ;//converts position of char c into integer (position of char in alphabet)
			int newNum =getKey() + convertLetter; //adds key + conversion of letter in alphabet
			if (newNum > 26) { //if the sum is greater than 26
				newNum = newNum -26;
			}
			//subtract 26 from total sum
			char b = (char)(newNum-1+'A');
			sb.append(b) ;

			i++;

		}
		while (i < message.length());
		newMessage = sb.toString();
		System.out.println(newMessage);//casts newNum to car and combines char with previous char

		return newMessage;
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
	}


	/**
	 * Decrypts a message, which consists of upper case letters only
	 *
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {
		StringBuilder sb = new StringBuilder();
		int i =0;
		String newMessage = ""; //initializes empty string
		System.out.println(newMessage);
		do {
			char c = message.charAt(i);
			int convertLetter = c-'A'+1; //converts position of char c into integer (position of char in alphabet)
			int newNum = convertLetter - getKey();
			if (newNum < 0) {
				newNum = newNum*-1;
			}
			//adds key + conversion of letter in alphabet
			if (newNum <= 26) { //if the sum is greater than 26
				newNum = (newNum +26)-(newNum - convertLetter);
			}//subtract 26 from total sum
			char b = (char)(newNum-1+'A');
			sb.append(b) ;

			i++;

		}
		while (i < message.length());
		newMessage = sb.toString();
		System.out.println(newMessage);//casts newNum to car and combines char with previous char

		return newMessage;

		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE

	}
}