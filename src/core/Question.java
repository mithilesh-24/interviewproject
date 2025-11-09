package core;

public class Question {
    public int id;
    private String question;
    private String company;
    private String college;
    private String difficulty;
    private String topic;
    public String createdBy;
    private String solution;

    public Question(int id, String question, String company, String college,
                    String difficulty, String topic, String createdBy, String solution) {
        this.id = id;
        this.question = question;
        this.company = company;
        this.college = college;
        this.difficulty = difficulty;
        this.topic = topic;
        this.createdBy = createdBy;
        this.solution = solution;
    }
    public Question(int id, String question, String company, String college,
                    String difficulty, String topic, String createdBy) {
        this(id, question, company, college, difficulty, topic, createdBy, "");
    }

    public int getId() { return id; }
    public String getQuestion() { return question; }
    public String getCompany() { return company; }
    public String getCollege() { return college; }
    public String getDifficulty() { return difficulty; }
    public String getTopic() { return topic; }
    public String getSolution() { return solution; }

    @Override
    public String toString() {
        return "     ID      : " + id +"\n"+
                "  Question  : " + question +"\n"+
                "  Company   : " + company +"\n"+
                "  College   : " + college +"\n"+
                " Difficulty : " + difficulty +"\n"+
                "   Topic    : " + topic+"\n"+
                "Created By  : " + createdBy+"\n"+
                "  Solution  : " + (solution.isEmpty() ? "N/A" : solution) + "\n";
    }
}
