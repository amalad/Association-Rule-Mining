# Association_Rule_Miner
Implementation of the apriori algorithm for frequent itemset generation and rule generation

#Language used:

We used Java to implement the Hash Tree and the Apriori algorithm.

#About the data:

Link to the dataset : http://tunedit.org/repo/UCI/vote.arff
The dataset consists of the votes of 435 US Congressmen on 16 key issues gathered in the mid-1980s, and also includes their party affiliation as a binary attribute. The missing values in the dataset correspond to abstentions. The 17 attributes and their possible values are as follows:
1. Handicapped-infants: 2(y,n)
2. water-project-cost-sharing: 2 (y,n)
3. adoption-of-the-budget-resolution: 2 (y,n)
4. physician-fee-freeze: 2 (y,n)
5. el-salvador-aid: 2 (y, n)
6. religious-groups-in-schools: 2 (y,n)
7. anti-satellite-test-ban: 2 (y,n)
8. aid-to-nicaraguan-contras: 2 (y,n)
9. mx-missile: 2 (y,n)
10. immigration: 2 (y,n)
11. synfuels-corporation-cutback: 2 (y,n)
12. education-spending: 2 (y,n)
13. superfund-right-to-sue: 2 (y,n)
14. crime: 2 (y,n)
15. duty-free-exports: 2 (y,n)
16. export-administration-act-south-africa: 2 (y,n)
17. Class Name: 2 (democrat, republican)

#Pre-processing:

The "vote.arff" file was pre-processed to generate a .txt file (named "opfile.txt") consisting of each Congressman's vote on a separate line. Each line consists of 17 integers, each denoting the attributes mentioned earlier in the same order. For attributes 1 to 16, 0 denotes a "No", 1 denotes a "Yes" and 2 denotes an abstention. For attribute 17 (the class name), 0 denotes the Republican class and 1 denotes the Democrat class.
So, the data can be treated as a list of transactions (each representing one Congressmen) of items (34 in all) representing the Boolean values of each attribute. Another file named "Items.txt" stores the name of each item on a separate line in the order of the integer values representing them. This was generated to print the corresponding item names (Here, the issues and the votes given on the issues) after rule generation.


#Compilation steps:
1. cd to the Assignment1 folder from the terminal.
2. Execute the following commands:
  a. javac RuleMiner.java
  b. java RuleMiner
3. Follow the on-screen prompts


#Frequent Itemsets and Rule Generation:

The Apriori algorithm was implemented for generating frequent itemsets and rules. HashTrees were implemented to perform support counting.

A sample output for minsup=0.45 and minconf=0.95 is displayed in the file "output.txt".
