package edu.buaa.rse.dotx.dbmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.Session;
import org.hibernate.query.Query;


@Entity
@Table(name = "task_result", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class TaskResultModel extends BaseModel{
	private Integer id;
	private Double sortkey;
	private TaskModel task;
	private String result;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 36)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getSortkey() {
		return sortkey;
	}

	public void setSortkey(Double sortkey) {
		this.sortkey = sortkey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskid", nullable = false)
	public TaskModel getTask() {
		return task;
	}

	public void setTask(TaskModel task) {
		this.task = task;
	}
	
	public TaskResultModel(){
		
	}
	
	public TaskResultModel(TaskModel task){
		this.task = task;
	}

	@Transient
	public static TaskResultModel getTaskResut(Session session, Integer id){
		String sql = "Select result from " + TaskResultModel.class.getName() + " result "
                + " where result.id= :id ";
        Query<TaskResultModel> query = session.createQuery(sql);
        query.setParameter("id", id);
        return (TaskResultModel) query.getSingleResult();
	}
	
	@Lob
	@Column(name="result")
	//@Column(name="result", columnDefinition="TEXT")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
