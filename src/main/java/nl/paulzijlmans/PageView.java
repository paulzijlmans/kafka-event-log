package nl.paulzijlmans;

import java.util.Date;

public class PageView {

  private String username;
  private String page;
  private String browser;
  private Date viewDate;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public String getBrowser() {
    return browser;
  }

  public void setBrowser(String browser) {
    this.browser = browser;
  }

  public Date getViewDate() {
    return viewDate;
  }

  public void setViewDate(Date viewDate) {
    this.viewDate = viewDate;
  }

  @Override
  public String toString() {
    return "PageView{" +
        "username='" + username + '\'' +
        ", page='" + page + '\'' +
        ", browser='" + browser + '\'' +
        ", viewDate='" + viewDate + '\'' +
        '}';
  }
}
