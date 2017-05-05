import java.util.*;
import java.io.*;

/**
 * @author Tanmaya
 * @param <KeyType> Type of the objects in the lists to be hashed
 */
class HashTree <KeyType>
{
    Hasher<KeyType> H;
    int max_leaf;
    int list_len;
    INode<KeyType> root;
    /**
     * Constructor for a hash tree
     * @param H The Hasher object which hashes a KeyType object to an integer
     * @param max_leaf The maximum number of entries in a leaf after which it splits
     * @param list_len The length of the object array
     */
    HashTree(Hasher<KeyType> H, int max_leaf, int list_len)
    {
        this.H=H;
        this.max_leaf=max_leaf;
        this.list_len=list_len;
        this.root=new INode<KeyType>(0);
    }
    /**
     * Add an array of objects to the tree
     * @param l The array of objects
     */
    void add(KeyType[] l)
    {
        if(l.length!=list_len)
        {
            System.err.println("Length does not match");
            System.exit(1);
        }
        root.add(l, max_leaf, H, list_len);
    }
    /**
     * Checks whether an array of objects is in the tree
     * @param l The array of objects to be checked
     * @return True or false depending on the existence of the array in the tree
     */
    boolean contains(KeyType[] l)
    {
        if(l.length!=list_len)
        {
            System.err.println("Length does not match");
            System.exit(1);
        }
        INode curr = root;
        Node x = root;
        for(int i=0; i<list_len; i++)
        {
            x = curr.getChild(H.hash(l[i]));
            if(x==null)
                return false;
            if(x.is_leaf)
                break;
            else
                curr=(INode)x;
        }
        LNode<KeyType> leaf=(LNode<KeyType>)x;
        for(KeyType[] j:leaf.list)
        {
            int k=0;
            for(; k<list_len; k++)
            {
                if(!j[k].equals(l[k]))
                    break;
            }
            if(k==list_len)
                return true;
        }
        return false;
    }
}

/**
 * Class abstracting the node of the tree
 * @author Tanmaya
 * @param <ItemType> The type of items to be hashed
 */
abstract class Node<ItemType>
{
    boolean is_leaf;
}
/**
 * Class representing the internal nodes of the tree
 * @author Tanmaya
 * @param <ItemType> The type of items to be hashed
 */
class INode<ItemType> extends Node<ItemType>
{
    HashMap<Integer, Node> children;
    int depth;
    /**
     * Constructor for an internal node
     * @param depth The depth of this node from root
     */
    INode(int depth)
    {
        this.depth=depth;
        this.is_leaf=false;
        children=new HashMap<Integer, Node>();
    }
    /**
     * Gets the child node of this node given a hash code
     * @param hashcode The hash code of the node needed
     * @return the child node or null if it doesn't exist
     */
    Node getChild(int hashcode)
    {
        return children.get(hashcode);
    }
    /**
     * Adds an array of objects to the subtree with this node as the root
     * @param l the array of objects to be added
     * @param max_leaf The maximum number of items before a split
     * @param h The Hasher object used to hash the objects
     * @param list_len The length of the array
     */
    void add(ItemType[] l, int max_leaf, Hasher<ItemType> h, int list_len)
    {
        int hashcode=h.hash(l[depth]);
        Node child=getChild(hashcode);
        if(child==null)
        {
            LNode<ItemType> x= new LNode<ItemType>(depth+1);
            x.add(l);
            children.put(hashcode, x);            
        }
        else if (!child.is_leaf)
        {
            INode x=(INode)child;
            x.add(l, max_leaf, h, list_len);
        }
        else
        {
            LNode<ItemType> x=(LNode<ItemType>)child;
            if(x.count<max_leaf || depth == list_len - 1)
            {
                x.add(l);
            }
            else
            {
                INode<ItemType> n=new INode<ItemType>(depth + 1);
                List<ItemType[]> list=x.list;
                children.remove(hashcode);
                for(ItemType[] i:list)
                {
                    n.add(i, max_leaf, h, list_len);
                }
                n.add(l, max_leaf, h, list_len);
                children.put(hashcode, n);
            }
        }
    }
}

/**
 * Class representing the leaf of the tree.
 * @author Tanmaya
 * @param <ItemType> The type of items to be hashed
 */
class LNode<ItemType> extends Node<ItemType>
{
    List<ItemType[]> list;
    int depth;
    int count;
    /**
     * Constructor for a leaf node
     * @param depth The depth of this node from root
     */
    LNode(int depth)
    {
        list=new ArrayList<ItemType[]>();
        this.depth=depth;
        this.count=0;
        this.is_leaf=true;
    }
    /**
     * Add an array of objects to this leaf node
     * @param l The array of objects
     */
    void add(ItemType[] l)
    {
        list.add(l);
        count++;
    }
    /**
     * Gets a list of all the arrays stored in this leaf
     * @return An ArrayList of the arrays of objects
     */
    List<ItemType[]> getList()
    {
        return list;
    }
}

/**
 * The interface for hashing a particular object type
 * @author Tanmaya
 * @param <ItemType> 
 */
interface Hasher<ItemType>
{
    /**
     * Hashes an object of ItemType type into integer
     * @param item The item to be hashed
     * @return The integer hashed value
     */
    public int hash(ItemType item);
}

/**
 * Hashes an object using the default Java hashcode() function
 * @author Tanmaya
 */
class DefaultHasher implements Hasher<Object>
{
    @Override
    public int hash(Object item)
    {
        return item.hashCode();
    }
}

class ModHasher implements Hasher<Integer>
{
    int k;
    ModHasher(int k)
    {
        this.k=k;
    }
    @Override
    public int hash(Integer item)
    {
        return item%k;
    }
}


/**
 * Class representing an itemset of Integer objects
 * @author Amala
 */
class Itemset 
{
	ArrayList<Integer> itemset;
	int count;
	
	/**
	 * Constructor for an Itemset
	 */
	Itemset()
	{
		this.itemset = new ArrayList<Integer>();
		this.count = 0;
	}

	/**
	 * Parameterized Constructor for an Itemset 
	 * @param itemset An ArrayList of Integers representing the itemset
	 */
	Itemset(ArrayList<Integer> itemset)
	{
		this.itemset = itemset;
		this.count = 0;
	}

	/**
	 * Add an item to the itemset
	 * @param item The Integer object to be added to the itemset
	 */
	void add(Integer item)
	{
		this.itemset.add(item);
	}

	/**
	 * Get the item at a particular index
	 * @param index The index from which the item has to be retrieved
	 * @return The Integer object at the specified index
	 */
	Integer getItem(int index)
	{
		return this.itemset.get(index);
	}

	/**
	 * Get the first num items from the itemset
	 * @param num The number of items to be retrieved
	 * @return An ArrayList of Integer objects
	 */
	ArrayList<Integer> getItems(int num)
	{
		ArrayList<Integer> items = new ArrayList<Integer>();
		for(int i=0; i<num; i++)
			items.add(this.itemset.get(i));
		return items;
	}

	/**
	 * Set the support count of the itemset
	 * @param count The value to which the support count has to be set
	 */
	void setCount(int count)
	{
		this.count = count;
	}

	/**
	 * Returns the support count of the itemset
	 * @return An integer denoting the support count of the itemset
	 */
	int getCount()
	{
		return this.count;
	}

	/**
	 * Returns the size of the itemset
	 * @return An integer whose value is equal to the size of the itemset
	 */
	int size()
	{
		return this.itemset.size();
	}

	/**
	 * Compares an Itemset with another Itemset for the first few specified items
	 * @param itemset The Itemset to be compared with
	 * @param len The number of items from the beginning to be compared
	 * @return True if the items match, false otherwise
	 */
	boolean compare(Itemset itemset, int len)
	{
		for(int i=0; i<len; i++)
			if(!this.itemset.get(i).equals(itemset.getItems(itemset.size()).get(i)))
				return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "("+itemset+", "+count+")";
	}

	@Override
	public boolean equals(Object object)
	{
		boolean isEqual= false;

		if (object != null && object instanceof Itemset)
		{
			isEqual = (this.itemset.equals(((Itemset) object).itemset));
		}

		return isEqual;
	}
}


/**
 * Class representing a Rule
 * @author Amala
 */
class Rule
{
	Itemset antecedant, consequent;
	double confidence;

	/**
	 * Parameterized constructor for a rule
	 * @param ant The antecedant itemset for the rule
	 * @param con The consequent itemset for the rule
	 * @param The confidence value of the rule
	 */
	Rule(Itemset ant, Itemset con, double conf)
	{
		antecedant = ant;
		consequent = con;
		confidence = conf;
	}

	@Override
	public String toString()
	{
		return "("+antecedant+", "+consequent+","+confidence+")";
	}
}


class RuleMiner{
	private static void getSubsets(ArrayList<Integer> superSet, int k, int idx, Set<Integer> current,ArrayList<Set<Integer>> solution) 
	{
		if (current.size() == k) {
			solution.add(new HashSet<>(current));
			return;
		}
		if (idx == superSet.size()) return;
		Integer x = superSet.get(idx);
		current.add(x);
		getSubsets(superSet, k, idx+1, current, solution);
		current.remove(x);
		getSubsets(superSet, k, idx+1, current, solution);
	}


	private static ArrayList<Set<Integer>> getSubsets(ArrayList<Integer> superSet, int k) 
	{
		ArrayList<Set<Integer>> res = new ArrayList<>();
		getSubsets(superSet, k, 0, new HashSet<Integer>(), res);
		return res;
	}


	private static ArrayList<ArrayList<Integer>> aprioriGen(ArrayList<Itemset> CFp)
	{
		ArrayList<ArrayList<Integer>> CF = new ArrayList<ArrayList<Integer>>();
		for(int i=0; i<CFp.size(); i++)
		{
			for(int j=i+1; j<CFp.size(); j++)
			{
				Itemset item1 = CFp.get(i);
				Itemset item2 = CFp.get(j);
				int flag = 1;
				if(item1.compare(item2, item1.size()-1))
				{
					ArrayList<Integer> newitemset = item1.getItems(item1.size()-1);
					newitemset.add(item1.itemset.get(item1.size()-1));
					newitemset.add(item2.itemset.get(item2.size()-1));
					Collections.sort(newitemset);
					for(int l=0; l<newitemset.size()-2; l++)
					{
						ArrayList<Integer> items = new ArrayList<Integer>();
						for(Integer in: newitemset)
							items.add(in);
						items.remove(l);
						Itemset temp = new Itemset(items);
						if(!CFp.contains(temp))
						{
							flag = 0; break;
						}
					}
					if(flag==0)
						continue;
					else
						CF.add(newitemset);
				}
			}
		}
		return CF;
	}


	public static ArrayList<ArrayList<Itemset>> getFrequentItemsets( ArrayList<ArrayList<Integer>> trn, int n_items, double minsup)
	{
		int k = 1;
		ArrayList<ArrayList<Itemset>> frequentISL = new ArrayList<ArrayList<Itemset>>();
		
		ArrayList<Itemset> F1 = new ArrayList<Itemset>();
		int count[] = new int[n_items];
		for(ArrayList<Integer> list: trn)
		{
			for(Integer I: list)
				count[I.intValue()]++;
		}
		for(int i=0; i<n_items; i++)
		{
			if(count[i] >= trn.size()*minsup)
			{
				Itemset itemset = new Itemset();
				itemset.add(new Integer(i));
				itemset.setCount(count[i]);
				F1.add(itemset);
			}
		}
		frequentISL.add(F1);
		while(true)
		{
			k++;
			HashTree<Integer> h = new HashTree<Integer>(new ModHasher(n_items), 3, k);
			ArrayList<Itemset> CFp = frequentISL.get(frequentISL.size()-1);
			ArrayList<ArrayList<Integer>> Fk = aprioriGen(CFp);
			for(ArrayList<Integer> newitemset: Fk)
			{
				Integer arr[] = new Integer[newitemset.size()];
				newitemset.toArray(arr);
				h.add(arr);
			}
			HashMap<ArrayList<Integer>, Integer> CF = new HashMap<ArrayList<Integer>, Integer>();
			for(ArrayList<Integer> array: Fk)
			{
				CF.put(array, new Integer(0));
			}
			
			for(int i=0; i<trn.size(); i++)
			{
				ArrayList<Integer> transaction = trn.get(i);
				ArrayList<Set<Integer>> subsets = getSubsets(transaction, k);
				int l = 0;
				for(Set<Integer> subset: subsets)
				{
					ArrayList<Integer> arrlist = new ArrayList<Integer>(subset);
					Collections.sort(arrlist);
					Integer arr[] = new Integer[arrlist.size()];
					arrlist.toArray(arr);
					if(h.contains(arr))
					{
						Integer in = new Integer(CF.get(arrlist).intValue() + 1);
						CF.put(arrlist, in);
					}
				}
			}
			
			ArrayList<Itemset> F = new ArrayList<Itemset>();
			Iterator<Map.Entry<ArrayList<Integer>, Integer>> iter = CF.entrySet().iterator();
			while(iter.hasNext())
			{
				Map.Entry<ArrayList<Integer>, Integer> entry = iter.next();
				if(entry.getValue().intValue()<minsup*435)
				{
					iter.remove();
				}
				else
				{
					Itemset tem = new Itemset(entry.getKey());
					tem.setCount(entry.getValue().intValue());
					F.add(tem);
				}
			}
			
			if(F.size()>=1)
			{
				frequentISL.add(F);
			}			

			if(F.isEmpty()||F.size()==1)
			break;
		}
		return frequentISL;
	}


	public static void apGenRules(ArrayList<ArrayList<Itemset>> F, Itemset fk, ArrayList<Itemset> Hm, double minconf, ArrayList<Rule> rules)
	{
		if(Hm.size()==0)
			return ;
		int k = fk.size(), m = Hm.get(Hm.size()-1).size();
		if(k>m+1)
		{
			ArrayList<ArrayList<Integer>> Hm1 = aprioriGen(Hm);
			for(int i=0; i<Hm1.size(); i++)
			{
				ArrayList<Integer> hm1 = Hm1.get(i);
				ArrayList<Integer> antc = new ArrayList<Integer>();
				for(Integer it: fk.itemset)
					if(!hm1.contains(it))
						antc.add(it);
				int tempc = 1;
				Itemset temp = new Itemset(antc);
				for(Itemset item: F.get(fk.size()-hm1.size()-1))
					if(item.equals(temp))
					{
						tempc = item.getCount(); break;
					}
				double conf = (double)(fk.getCount())/tempc;
				if(conf>=minconf)
				{
					temp.setCount(tempc);
					int consc = 1;
					Itemset cons = new Itemset(hm1);
					for(Itemset item: F.get(hm1.size()-1))
						if(item.equals(cons))
						{
							consc = item.getCount(); break;
						}
					cons.setCount(consc);
					Rule rule = new Rule(temp, cons, conf);
					rules.add(rule);
				}
				else
				{
					Hm1.remove(i);
					i--;
				}
				
			}
			ArrayList<Itemset> HM1 = new ArrayList<Itemset>();
			for(ArrayList<Integer> item: Hm1)
				HM1.add(new Itemset(item));
			apGenRules(F, fk, HM1, minconf, rules);
		}
	}


	public static ArrayList<Rule> ruleGen(ArrayList<ArrayList<Itemset>> F, int n_items, double minconf)
	{
		ArrayList<Rule> rules = new ArrayList<Rule>();
		for(int k=1; k<F.size(); k++)
		{
			ArrayList<Itemset> Fk = F.get(k);
			for(Itemset fk: Fk)
			{
				ArrayList<Itemset> H1 = new ArrayList<Itemset>();
				for(int i=0; i<fk.size(); i++)
				{
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.add(fk.getItem(i));
					H1.add(new Itemset(temp));
					for(Itemset itemset: F.get(0))
						if(itemset.equals(H1.get(H1.size()-1)))
							H1.get(H1.size()-1).setCount(itemset.getCount());
				}
				for(int i=0; i<H1.size(); i++)
				{
					Itemset h1 = H1.get(i);
					ArrayList<Integer> antc = new ArrayList<Integer>();
					for(Integer it: fk.itemset)
						if(!h1.itemset.contains(it))
							antc.add(it);
					int tempc = 1;
					Itemset temp = new Itemset(antc);
					for(Itemset item: F.get(fk.size()-2))
						if(item.equals(temp))
						{
							tempc = item.getCount(); break;
						}
					double conf = (double)(fk.getCount())/tempc;
					if(conf>=minconf)
					{
						temp.setCount(tempc);
						Rule rule = new Rule(temp, h1, conf);
						rules.add(rule);
					}
					else
					{
						H1.remove(i);
						i--;
					}
				}
				apGenRules(F, fk, H1, minconf, rules);
			}
		}
		return rules;
	}


	public static void main(String args[])
	{
		ArrayList<ArrayList<Integer>> trn = new ArrayList<ArrayList<Integer>>();
		int items = 34;
		String file = "opfile.txt", file1 = "Items.txt";
		String item_names[] = new String[items];
		try{
			Scanner sc = new Scanner(new File(file));
			int i = 0, temp = 0;
			System.out.println("Reading file...");
			while(sc.hasNextInt())
			{
				if(i%17 == 0)
					trn.add(new ArrayList<Integer>());
				temp = sc.nextInt();
				if(temp == 0)
					trn.get(i/17).add((i%17)*2);
				else if(temp == 1)
					trn.get(i/17).add((i%17)*2+1);
				i++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
    			String line;
			int i=0;
    			while ((line = br.readLine()) != null) 
			{
       				item_names[i++] = line;
    			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}

		//Scanner scan = new Scanner(System.in);
		//System.out.print("Enter minsup value: ");
		double minsup = 0.45;//scan.nextDouble();
		//System.out.print("Enter minconf value: ");
		double minconf = 0.95;//scan.nextDouble();

		ArrayList<ArrayList<Itemset>> frequentISL = getFrequentItemsets(trn, items, minsup);
		System.out.println("The frequent itemsets generated are as follows:");
		for(ArrayList<Itemset> array: frequentISL)
		{
			for(Itemset it: array)
			{
				System.out.print("[ ");
				for(Integer in: it.itemset)
					System.out.print(item_names[in.intValue()]+" ");
				System.out.println("] "+ it.getCount());
			}
		}
		ArrayList<Rule> rules = ruleGen(frequentISL, items, minconf);
		System.out.println();
		System.out.println("The rules generated are as follows:");
		for(Rule rule: rules)
		{
			for(Integer it: rule.antecedant.itemset)
				System.out.print(item_names[it.intValue()]+" ");
			System.out.print("("+rule.antecedant.getCount()+") => ");
			for(Integer it: rule.consequent.itemset)
				System.out.print(item_names[it.intValue()]+" ");
			System.out.println("("+rule.consequent.getCount()+") conf("+rule.confidence+")");
		}
	}
}
