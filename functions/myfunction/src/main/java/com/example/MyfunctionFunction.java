package com.example;

import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import com.salesforce.functions.jvm.sdk.SalesforceFunction;
import com.salesforce.functions.jvm.sdk.data.Record;
import com.salesforce.functions.jvm.sdk.data.RecordWithSubQueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import com.amazon.pay.api.*;

import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.types.Environment;
import com.amazon.pay.api.types.Region;

/**
 * Describe MyfunctionFunction here.
 */
public class MyfunctionFunction implements SalesforceFunction<FunctionInput, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(MyfunctionFunction.class);

  @Override
  public FunctionOutput apply(InvocationEvent<FunctionInput> event, Context context)
      throws Exception {

      PayConfiguration payConfiguration = null;
      try {
          payConfiguration = new PayConfiguration()
              .setPublicKeyId("YOUR_PUBLIC_KEY_ID")
              .setRegion(Region.NA)
              .setPrivateKey("YOUR_PRIVATE_KEY_STRING")
              .setEnvironment(Environment.SANDBOX);
      }catch (AmazonPayClientException e) {
            return new FunctionOutput(e.printStackTrace());
//           e.printStackTrace();
      }
      System.out.println("111 payConfiguration=" + payConfiguration);


    List<RecordWithSubQueryResults> records =
        context.getOrg().get().getDataApi().query("SELECT Id, Name FROM Account").getRecords();

    LOGGER.info("Function successfully queried {} account records!", records.size());

    List<Account> accounts = new ArrayList<>();
    for (Record record : records) {
      String id = record.getStringField("Id").get();
      String name = record.getStringField("Name").get();

      accounts.add(new Account(id, name));
    }

    return new FunctionOutput(accounts);
  }
}
