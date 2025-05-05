package CRDT;

public class Comment
{

        private final String author;
        private final String message;
        private final long timestamp;

        public Comment(String author, String message)
        {
            this.author = author;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getAuthor() { return author; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }

}
