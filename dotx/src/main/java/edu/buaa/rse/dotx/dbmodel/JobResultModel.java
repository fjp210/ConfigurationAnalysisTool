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
import javax.persistence.UniqueConstraint;

import org.hibernate.Session;
import org.hibernate.query.Query;


@Entity
@Table(name = "job_result", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class JobResultModel extends BaseModel{
	private Integer id;
	private String type;
	private Double sortkey;
	private JobModel job;
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

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getSortkey() {
		return sortkey;
	}

	public void setSortkey(Double sortkey) {
		this.sortkey = sortkey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobid", nullable = false)
	public JobModel getJob() {
		return job;
	}

	public void setJob(JobModel job) {
		this.job = job;
	}
	
	public JobResultModel(){
		
	}
	
	public JobResultModel(JobModel job){
		this.job = job;
	}

	public static JobResultModel getJobResut(Session session, Integer id){
		String sql = "Select result from " + JobResultModel.class.getName() + " result "
                + " where result.id= :id ";
        Query<JobResultModel> query = session.createQuery(sql);
        query.setParameter("id", id);
        return (JobResultModel) query.getSingleResult();
	}
	
	@Lob
	@Column(name="result")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
