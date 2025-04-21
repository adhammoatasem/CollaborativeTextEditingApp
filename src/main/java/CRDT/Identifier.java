package CRDT;
import java.util.Objects;

public class Identifier implements Comparable<Identifier>
{

  private final String userID;
  private final long counter;

    public Identifier(String userID, long counter) {
        this.userID = userID;
        this.counter = counter;
    }

    public String getUserID() {
        return userID;
    }
    public long getCounter() {
        return counter;
    }
    @Override
    public int compareTo(Identifier other)
    {
        int c = Long.compare(this.counter, other.counter);
        if (c != 0) // not equal (mesh byektebo f nafs elwa2t)
        {
            return c; // -1 -> other akbar meny , 1-> ana akbar
        }
        else
        { return this.userID.compareTo(other.userID); } // law byektebo f nafs elwa2t hay compare between userIDS

    }


    @Override
    public boolean equals(Object o) // to be able to equalize or compare two identifiers so that we compare its counter or user ID
    {
        if (this == o) return true;
        if (!(o instanceof Identifier)) return false;
        Identifier x = (Identifier) o;
        if (counter==x.counter && userID.equals(x.userID))
        { return true ;}
        else
        {return false;}

    }


    @Override
    public int hashCode() // to make sure identifier with the same userid and counter have the same hashcode()
    {
        return Objects.hash(userID, counter); //generate hashcode
    }

    @Override
    public String toString()  // ashan yetla3 f shakl ( user1,2) -> user one typed the second character
    {
        return "(" + userID + ", " + counter + ")";
    }

}
