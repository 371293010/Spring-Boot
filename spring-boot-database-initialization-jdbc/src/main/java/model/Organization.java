package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Organization {

  protected Organization() {}

  /**
   * Constructor.
   * 
   * @param companyName String
   * @param active boolean
   */
  public Organization(String companyName, boolean active) {
    super();
    this.companyName = companyName;
    this.active = active;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "company_name", nullable = false, length = 30)
  private String companyName;

  @Column(name = "active", nullable = false)
  private boolean active;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

}
