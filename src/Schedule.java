
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Schedule {

    private ArrayList<Job> jobList;

    public Schedule() {
        jobList = new ArrayList<>();
    }

    public Job insert(int time) {
        Job newJob = new Job();
        newJob.time = time;
        newJob.inDegree = 0;
        jobList.add(newJob);
        return newJob;
    }

    public int finish() {
        int maxTime = -1;
        jobList.get(0).start();
        for (Job job : jobList) {
            int startTime = job.timeFinish;
            if (startTime == -1) {
                return -1;
            }
            startTime += job.time;
            if (startTime > maxTime) {
                maxTime = startTime;
            }
        }
        return maxTime;
    }

    private void relax(Job cur, Job j) {
        if (cur.timeFinish + cur.time > j.timeFinish) {
            j.timeFinish = cur.timeFinish + cur.time;
        }
    }

    private ArrayList<Job> topOrder() {
        initialize();
        Queue<Job> vertList = new LinkedList<>();
        ArrayList<Job> result = new ArrayList<>();
        for (Job job : jobList) {
            if (job.inDegree == 0) {
                vertList.add(job);
                job.timeFinish = 0;
            }
        }
        while (!vertList.isEmpty()) {
            Job job = vertList.poll();
            result.add(job);
            for (Job obj : job.out) {
                obj.inDegree--;
                if (obj.inDegree == 0) {
                    vertList.add(obj);
                    obj.timeFinish = 0;
                }
            }
        }
        return result;
    }

    private void initialize() {
        for (Job job : jobList) {
            countInDegree(job);
            job.timeFinish = -1;
        }
    }

    public void countInDegree(Job j) {
        j.inDegree = j.in.size();
    }

    public int shortestPath(Job j) {
        ArrayList<Job> topList = topOrder();
        for (Job j1 : topList) {
            for (Job job : j1.out) {
                if(job.timeFinish != -1)
                    relax(j1, job);
            }
        }
        return j.timeFinish;
    }

    public Job get(int index) {
        return jobList.get(index);
    }

    class Job {

        private ArrayList<Job> out = new ArrayList<>();
        private ArrayList<Job> in = new ArrayList<>();
        private int time, inDegree, timeFinish;

        public void requires(Job j) {
            j.out.add(this);
            in.add(j);
        }

        public int start() {
            return shortestPath(this);
        }

    }
}
