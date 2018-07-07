package edu.buaa.rse.dotx.dbmodel;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.query.Query;

@Entity
@Table(name = "job", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class JobModel extends BaseModel{
	private Integer id;
	private TaskModel task;
	private Integer status;
	private Set<JobResultModel> results = new HashSet<JobResultModel>(0);

	
	public JobModel(){
		
	}
	
	public JobModel(TaskModel task){
		this.task = task;
	}
	
	public static JobModel getJob(Session session, Integer id){
		String sql = "Select job from " + JobModel.class.getName() + " job "
                + " where job.id= :id ";
        Query<JobModel> query = session.createQuery(sql);
        query.setParameter("id", id);
        return (JobModel) query.getSingleResult();
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskid", nullable = false)
	public TaskModel getTask() {
		return task;
	}

	public void setTask(TaskModel task) {
		this.task = task;
	}

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
	public Set<JobResultModel> getResults() {
		return results;
	}

	public void setResults(Set<JobResultModel> results) {
		this.results = results;
	}

	@Column(name="status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


}
