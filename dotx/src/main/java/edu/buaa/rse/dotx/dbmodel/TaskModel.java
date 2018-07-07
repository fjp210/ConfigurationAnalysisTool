package edu.buaa.rse.dotx.dbmodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.Session;
import org.hibernate.query.Query;

@Entity
@Table(name = "task", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class TaskModel extends BaseModel{
	private Integer id;
	private Integer status;
	private String tempDir;
	private Set<JobModel> jobs = new HashSet<JobModel>(0);
	
	public TaskModel(){
		
	}
	
	public static TaskModel getTask(Session session, Integer id){
		String sql = "Select task from " + TaskModel.class.getName() + " task "
                + " where task.id= :id ";
        Query<TaskModel> query = session.createQuery(sql);
        query.setParameter("id", id);
        return (TaskModel) query.getSingleResult();
	}
	
	public void updateStatus(){
		Map<String, Integer> statuses = this.getJobStatus();
		if(statuses.get("finished").equals(statuses.get("total"))){
			this.setStatus(2);
		}else if(statuses.get("inited").equals(statuses.get("total"))){
			this.setStatus(0);
		}else{
			this.setStatus(1);
		}
	}
	
	@Transient
	public Map<String, Integer> getJobStatus(){
		Map<String,Integer> statusList = new HashMap<String, Integer>();
		int inited=0, running=0, finished=0, failed=0, total=0;
		Set<JobModel> jobs = this.getJobs();
		for(JobModel job:jobs){
			Integer status = job.getStatus();
			if(status.equals(0)){
				inited++;
			}else if(status.equals(1)){
				running++;
			}else if(status.equals(2)){
				finished++;
			}else{
				failed++;
			}
			total++;
		}
		statusList.put("inited", inited);
		statusList.put("running", running);
		statusList.put("finished", finished);
		statusList.put("failed", failed);
		statusList.put("total", total);
		return statusList;
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 36)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
	public Set<JobModel> getJobs() {
		return jobs;
	}

	public void setJobs(Set<JobModel> jobs) {
		this.jobs = jobs;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name="temp_dir")
	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}


	
	
	
}
