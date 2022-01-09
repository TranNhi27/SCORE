package devera.score.example;

import score.Address;
import score.Context;
import score.DictDB;
import score.VarDB;
import score.ArrayDB;
import java.util.List;
import score.annotation.EventLog;
import score.annotation.External;
import score.annotation.Payable;

import java.math.BigInteger;
import java.util.Map;

import scorex.util.ArrayList;

public class DeveraCourse {
    private Address iconFoundation;
    private final long deadline;
    private boolean activeCourse;
    private BigInteger tuitionFee;
    private final DictDB<Address, BigInteger> balances;
    private final VarDB<BigInteger> tuitionCourse;
    private BigInteger teacherSalary;
    private final ArrayDB<Student> students = Context.newArrayDB("students", Student.class);
    private Address teacherAddress;

    public DeveraCourse(BigInteger _tuitionFeePerStudent, BigInteger _durationInBlocks, BigInteger _salaryForTeacherInICX, Address _teacherAddress) {
        Context.require(_tuitionFeePerStudent.compareTo(BigInteger.ZERO) >= 0);
        Context.require(_durationInBlocks.compareTo(BigInteger.ZERO) >= 0);
        Context.require(_salaryForTeacherInICX.compareTo(BigInteger.ZERO) >= 0);

        this.tuitionFee = _tuitionFeePerStudent;
        this.deadline = _durationInBlocks.longValue();
        this.teacherSalary = _salaryForTeacherInICX;
        this.teacherAddress = _teacherAddress;

        this.activeCourse = false;

        this.iconFoundation = Context.getCaller();

        
        this.balances = Context.newDictDB("balances", BigInteger.class);
        this.tuitionCourse = Context.newVarDB("tuitionCourse", BigInteger.class);

    }

    @External(readonly=true)
    public String name() {
        return "Devera ICON Course";
    }

    @External(readonly=true)
    public String description() {
        return "Devera ICON dapp development course";
    }

    @External(readonly=true)
    public BigInteger balanceOf(Address _owner) {
        return this.safeGetBalance(_owner);
    }

    @External(readonly=true)
    public BigInteger tuitionRaised() {
        return this.tuitionCourse.get();
    }

    // @External(readonly=true)
    // public void students() {
    //     if (this.students.size() == 0) {
    //         Context.revert("Students list not found");
    //     }
    //     for (int i = 0; i < students.size(); i++)
    //     {
    //         return this.students.get(i).toMap();
    //     }
        
    // }
 
    @External
    public void proveAttendance() {
        Address _from = Context.getCaller();
        Context.require(this.activeCourse);
        for (int i = 0; i < students.size(); i++)
        {
            if (students.get(i).getStudentAddress().equals(_from))
            {
                students.set(i, new Student(_from, students.get(i).getAttendance().add(BigInteger.valueOf(1)), students.get(i).getTuitionFee()));
                StudentProveAttendance(_from, students.get(i).getAttendance());
            }
        } 
    } 

    @Payable
    public void fallback() {
        Address _from = Context.getCaller();
        BigInteger _value = Context.getValue();
        Context.require(_value.compareTo(BigInteger.ZERO) > 0);

        //if caller is owner of this Score (iconFoundation)
        if (this.iconFoundation.equals(_from) && this.teacherSalary.equals(_value))
        {
            this.activeCourse = true;
            CourseStarted(this.tuitionFee, this.deadline);
            this.balances.set(_from, _value);
        }

        if (this.iconFoundation.equals(_from) && !this.teacherSalary.equals(_value))
        {
            BigInteger fund = safeGetBalance(_from);
            this.balances.set(_from, fund.add(_value));
        }
        
        if(!this.iconFoundation.equals(_from))
        {
            //require Devera Course to be active
            Context.require(this.activeCourse);
            for (int i = 0; i < students.size(); i++)
            {
                if (students.get(i).getStudentAddress().equals(_from))
                {
                    Context.revert("Student has signed to this course");
                }
            }
            if (!_value.equals(tuitionFee))
            {
                Context.revert("Insuffient tuition fee for the course");
            }
            
            // accept and save students
            BigInteger fromBalance = safeGetBalance(_from);
            Student newStudent = new Student(_from, BigInteger.valueOf(0), _value);
            this.students.add(newStudent);
            
            // increase the total amount of tuitionCourse
            BigInteger tuitionCourse = safeGetTuitionRaised();
            this.tuitionCourse.set(tuitionCourse.add(_value));

            StudentSignedCourse(_from, _value);
        }
    }

    @External
    public void studentWithdrawal() {
        Address _from = Context.getCaller();
        for (int i = 0; i < students.size(); i++)
        {
            if (students.get(i).getStudentAddress().equals(_from))
            {
                if (!afterDeadline())
                {
                    Context.revert("Can not withdraw before the course's endtime");
                }
                if (students.get(i).getAttendance().compareTo(BigInteger.valueOf(3)) < 0)
                {
                    Context.revert("Student didn't present enough during class");
                }

                Context.transfer(_from, tuitionFee);
                this.tuitionCourse.set(this.tuitionCourse.get().subtract(tuitionFee));
                //emit event
                StudentWithdrawal(_from, tuitionFee, students.get(i).getAttendance());
                //student should withdraw only once 
                students.set(i, null);
            }
            else {
                Context.revert("unauthorized");
            } 
        } 
    }

    @External
    public void teacherWithdrawal() {
        Address _from = Context.getCaller();
        Context.require(this.teacherAddress.equals(_from));
        Context.require(this.activeCourse);
        if (!afterDeadline())
        {
            Context.revert("Can not withdraw before the course's endtime");
        }
  
        Context.transfer(_from, teacherSalary);
          
        //emit event
        TeacherWithdrawal(_from, teacherSalary);

        //after teacher withdrawal, the course finish
        this.activeCourse = false;
    }
    
    
    private boolean afterDeadline() {
        // checks if it has been reached to the deadline block
        return Context.getBlockHeight() >= this.deadline;
    }
 
    private BigInteger safeGetBalance(Address owner) {
        return this.balances.getOrDefault(owner, BigInteger.ZERO);
    }

    private BigInteger safeGetTuitionRaised() {
        return this.tuitionCourse.getOrDefault(BigInteger.ZERO);
    }

    @EventLog
    public void CourseStarted(BigInteger fundingGoal, long deadline) {}

    @EventLog(indexed=2)
    public void StudentSignedCourse(Address studentAddress, BigInteger courseFee) {}

    @EventLog(indexed=2)
    public void StudentProveAttendance(Address studentAddress, BigInteger attendance) {}

    @EventLog(indexed=3)
    public void StudentWithdrawal(Address studentAddress, BigInteger fee, BigInteger attendance) {}

    @EventLog(indexed=2)
    public void TeacherWithdrawal(Address teacherAddress, BigInteger salary) {}
}
