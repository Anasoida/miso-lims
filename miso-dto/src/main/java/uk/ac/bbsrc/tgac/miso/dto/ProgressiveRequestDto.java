package uk.ac.bbsrc.tgac.miso.dto;

/**
 * Request incremental results from a run scanner.
 * 
 * The goal of the progressive requests is to avoid sending unchanged data from a run scanner to a client. That being said, the run scanner
 * has no qualms about sending duplicate information to the client and the client must deal with that.
 * 
 * After a request is made, the next request should use the {@link #update(ProgressiveResponseDto)} method to request only the subsequent
 * data.
 * 
 * Otherwise, the epoch and token value should be initialised to zero.
 */
public class ProgressiveRequestDto {
  private static final int DEFAULT_LIMIT = 100;
  private int epoch;
  private int limit = DEFAULT_LIMIT;
  private long token;

  public int getEpoch() {
    return epoch;
  }

  public int getLimit() {
    return limit < 1 ? DEFAULT_LIMIT : limit;
  }

  public long getToken() {
    return token;
  }

  public void setEpoch(int epoch) {
    this.epoch = epoch;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public void setToken(long token) {
    this.token = token;
  }

  public void update(ProgressiveResponseDto response) {
    token = response.getToken();
    epoch = response.getEpoch();
  }
}
