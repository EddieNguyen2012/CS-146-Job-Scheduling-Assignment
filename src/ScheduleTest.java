/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eddie
 */
public class ScheduleTest {

    public ScheduleTest() {
    }

    @Test
    public void basicGraphTests() {
        Schedule schedule = new Schedule();
        schedule.insert(8); //adds job 0 with time 8
        Schedule.Job j1 = schedule.insert(3); //adds job 1 with time 3
        schedule.insert(5); //adds job 2 with time 5
        assertEquals(8, schedule.finish()); //should return 8, since job 0 takes time 8 to complete.
        /* Note it is not the earliest completion time of any job, but the earliest the entire set can complete. */
        schedule.get(0).requires(schedule.get(2)); //job 2 must precede job 0
        assertEquals(13, schedule.finish()); //should return 13 (job 0 cannot start until time 5)
        schedule.get(0).requires(j1); //job 1 must precede job 0
        assertEquals(13, schedule.finish()); //should return 13
        assertEquals(5, schedule.get(0).start()); //should return 5
        assertEquals(0, j1.start()); //should return 0
        assertEquals(0, schedule.get(2).start()); //should return 0
        j1.requires(schedule.get(2)); //job 2 must precede job 1
        assertEquals(16, schedule.finish()); //should return 16
        assertEquals(8, schedule.get(0).start()); //should return 8
        assertEquals(5, schedule.get(1).start()); //should return 5
        assertEquals(0, schedule.get(2).start()); //should return 0
    }

    @Test
    public void loopGraphTest() {
        Schedule schedule = new Schedule();
        schedule.insert(8); //adds job 0 with time 8
        Schedule.Job j1 = schedule.insert(3); //adds job 1 with time 3
        schedule.insert(5); //adds job 2 with time 5
        schedule.get(0).requires(j1); //job 1 must precede job 0
        schedule.get(0).requires(schedule.get(2)); //job 2 must precede job 0
        schedule.get(1).requires(schedule.get(0)); //job 0 must precede job 1 (creates loop)
        assertEquals(-1, schedule.finish()); //should return -1
        assertEquals(-1, schedule.get(0).start()); //should return -1
        assertEquals(-1, schedule.get(1).start()); //should return -1
        assertEquals(0, schedule.get(2).start()); //should return 0 (no loops in prerequisites)
    }
    
    @Test
    public void longLoopTest() {
        Schedule schedule = new Schedule();
        schedule.insert(8); 
        Schedule.Job j1 = schedule.insert(3); 
        schedule.insert(5); 
        schedule.get(0).requires(schedule.get(1));
        schedule.get(1).requires(schedule.get(2));
        schedule.get(2).requires(schedule.get(0));
        assertEquals(-1, schedule.finish());
        assertEquals(-1, schedule.get(0).start()); 
        assertEquals(-1, schedule.get(1).start());
        assertEquals(-1, schedule.get(2).start());
        
    }
    
    @Test
    public void longerThanTheAboveLoopTest() {
        Schedule schedule = new Schedule();

        Schedule.Job job1 = schedule.insert(3);
        Schedule.Job job2 = schedule.insert(4);
        Schedule.Job job3 = schedule.insert(5);
        Schedule.Job job4 = schedule.insert(2);

        // Job1 depends on Job2, Job2 depends on Job3, and Job3 depends on Job4
        job1.requires(job2);
        job2.requires(job3);
        job3.requires(job4);

        // Adding a circular dependency from Job4 back to Job1
        job4.requires(job1);

        assertEquals(-1, schedule.finish());
    }
}
