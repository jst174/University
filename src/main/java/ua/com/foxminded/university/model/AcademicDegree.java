package ua.com.foxminded.university.model;

public enum AcademicDegree {

	ASSOCIATE{
        public int getMaxVacationPeriod(){
            return 15;
        }
    },
    BACHELOR{
        public int getMaxVacationPeriod(){
            return 20;
        }
    },
    MASTER{
        public int getMaxVacationPeriod(){
            return 25;
        }
    },
    DOCTORAL{
        public int getMaxVacationPeriod(){
            return 30;
        }
    };
    public abstract int getMaxVacationPeriod();
}
