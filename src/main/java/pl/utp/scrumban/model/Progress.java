package pl.utp.scrumban.model;

public enum Progress {
    BACKLOG(0, false),
    QA(1, true),
    DEVELOPMENT(2, true),
    TEST(3, true),
    DEPLOYMENT(4, true),
    DONE(5, false);

    private final int value;
    private final boolean inProgress;

    Progress(int value, boolean inProgress) {
        this.value = value;
        this.inProgress = inProgress;
    }

    public int getValue() {
        return value;
    }

    public boolean getInProgress() {
        return inProgress;
    }
}