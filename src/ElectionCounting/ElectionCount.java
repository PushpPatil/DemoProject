package ElectionCounting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import bloomfilter.BloomFilterImpl;

public class ElectionCount {

	BloomFilterImpl bf;
	HashMap candidateCountMap;
	ArrayList notVotedList =  new ArrayList();
	ArrayList invalidVotersList =  new ArrayList();
	HashMap voterCandidateMap;

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {

		ElectionCount ec = new ElectionCount();
		String fileName = "C:\\\\Users\\\\karib\\\\Downloads\\\\Problem\\\\validVotersList.txt";
		ec.readVotersList(fileName);
		ec.add();
		
		System.out.println("voterCandidateMap :: "+ec.voterCandidateMap);
		System.out.println("notVotedList :: "+ec.notVotedList);
		System.out.println("invalidVotersList :: "+ec.invalidVotersList);
		

	}

	public void readVotersList(String fileName) {
		int i = 0;
		

		try {
			File file = new File("C:\\Users\\karib\\Downloads\\Problem\\validVotersList.txt");

			FileReader fr = new FileReader(file); 
			BufferedReader br = new BufferedReader(fr); 
			StringBuffer sb = new StringBuffer(); 
			String line;

			while ((line = br.readLine()) != null) {
				i++;
				sb.append(line); 
				sb.append("\n"); 
			}
			fr.close();

			ArrayList<String> al = new ArrayList<String>(i);
			String[] strArray = sb.toString().split("\\n");

			bf = new BloomFilterImpl(i, 100);

			for (int j = 0; j < i; j++) {
				bf.add(new Integer(strArray[j]));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void add() {

		try {
			File file = new File("C:\\Users\\karib\\Downloads\\Problem\\votersCandList.txt");

			FileReader fr = new FileReader(file); // reads the file
			BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
			StringBuffer sb = new StringBuffer(); // constructs a string buffer with no characters
			candidateCountMap = new HashMap<String, String>();
			
			String voterCandidateline;
			int i = 0;
			int firstVote = 1;
			int voterId = 0;
			int candidateId = 0;
			int voterIdIndex = 0;
			int candidateIdIndex = 1;

			while ((voterCandidateline = br.readLine()) != null) {
				i++;

				String[] str = voterCandidateline.split(" ");

				if (str.length >= 2) {
					voterId = Integer.valueOf(str[voterIdIndex]);
					candidateId = Integer.valueOf(str[candidateIdIndex]);
					add(voterId, candidateId);
				} else {
					notVotedList.add(voterId);
				}

			}
			fr.close();
			System.out.println("Final List " + candidateCountMap);
		}

		catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void add(int voterId, int candidateId) {
	int firstVote = 1;

		if (bf.contains(voterId)) {

			if (candidateCountMap.containsKey(candidateId)) {

				int voteCount = Integer.valueOf(candidateCountMap.get(candidateId).toString());

				candidateCountMap.put(candidateId, ++voteCount);
			} else
				candidateCountMap.put(candidateId, firstVote);

		} else {
			invalidVotersList.add(voterId);
		}

	}

	@SuppressWarnings("unchecked")
	public int find(int voterId) {
		int candidateId = 0;
		
		if (bf.contains(voterId)) {
			return candidateId;
		}else {
			invalidVotersList.add(voterId);
			System.out.println("Invalid Voter");
		}
		return candidateId;

	}

	public int count(int candidateId) {
		int numberOfVotes = 0;

		if(candidateCountMap.containsKey(candidateId))
			return Integer.valueOf(candidateCountMap.get(candidateId).toString());
		else 
			return numberOfVotes;
			

	}
}
