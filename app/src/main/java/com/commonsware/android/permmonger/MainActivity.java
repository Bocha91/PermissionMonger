/*
 Copyright (c) 2015 CommonsWare, LLC
 Licensed under the Apache License, Version 2.0 (the "License"); you may not
 use this file except in compliance with the License. You may obtain a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 by applicable law or agreed to in writing, software distributed under the
 License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 OF ANY KIND, either express or implied. See the License for the specific
 language governing permissions and limitations under the License.

 Covered in detail in the book _The Busy Coder's Guide to Android Development_
 https://commonsware.com/Android
 */
// Этот пример я нашол в интернете, на нем пртренировался с предоставлением разрешений приложению
package com.commonsware.android.permmonger;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
/*  private static final String[] INITIAL_PERMS={
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.READ_CONTACTS
  };
  */
  private static final String[] CAMERA_PERMS={
    Manifest.permission.CAMERA
  };
  private static final String[] CONTACTS_PERMS={
      Manifest.permission.READ_CONTACTS
  };
  private static final String[] LOCATION_PERMS={
      Manifest.permission.ACCESS_FINE_LOCATION
  };
  private static final String[] STORAGE_PERMS={
          Manifest.permission.WRITE_EXTERNAL_STORAGE
  };
  private static final String[] EXTRA_PERMS={
          Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
  };
  private static final String[] PHONE1_PERMS={
          Manifest.permission.CALL_PHONE
  };
  private static final String[] PHONE2_PERMS={
          Manifest.permission.READ_PHONE_STATE
  };
  private static final String[] SMS_PERMS={
          Manifest.permission.READ_SMS,
          Manifest.permission.BROADCAST_SMS
  };
  private static final String[] INTERNET_PERMS={
          Manifest.permission.INTERNET
  };

  private static final int INITIAL_REQUEST =1337;
  private static final int CAMERA_REQUEST  =INITIAL_REQUEST+1;
  private static final int CONTACTS_REQUEST=INITIAL_REQUEST+2;
  private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;
  private static final int STORAGE_REQUEST =INITIAL_REQUEST+4;
  private static final int EXTRA_REQUEST   =INITIAL_REQUEST+5;
  private static final int PHONE1_REQUEST  =INITIAL_REQUEST+6;
  private static final int PHONE2_REQUEST  =INITIAL_REQUEST+7;
  private static final int SMS_REQUEST     =INITIAL_REQUEST+8;
  private static final int INTERNET_REQUEST=INITIAL_REQUEST+9;
  private TextView location;
  private TextView camera;
  private TextView internet;
  private TextView contacts;
  private TextView storage;
  private TextView extra;
  private TextView phone1;
  private TextView phone2;
  private TextView sms;
  //private TextView internet;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    location=(TextView)findViewById(R.id.location_value);
    camera  =(TextView)findViewById(R.id.camera_value);
    internet=(TextView)findViewById(R.id.internet_value);
    contacts=(TextView)findViewById(R.id.contacts_value);
    storage =(TextView)findViewById(R.id.storage_value);
    extra   =(TextView)findViewById(R.id.extra_value);
    phone1  =(TextView)findViewById(R.id.phone1_value);
    phone2  =(TextView)findViewById(R.id.phone2_value);
    sms     =(TextView)findViewById(R.id.sms_value);

/*    if (!canAccessLocation() || !canAccessContacts()) {
      requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
    }*/

  }

  @Override
  protected void onResume() {
    super.onResume();

    updateTable();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.actions, menu);

    return(super.onCreateOptionsMenu(menu));
  }
  private void TestMenu(Menu menu,int id,boolean flag){
    MenuItem register = menu.findItem(id);
    if(flag) register.setVisible(false);
    else register.setVisible(true);
  }
  @Override
  public boolean onPrepareOptionsMenu(Menu menu)
  {
    TestMenu(menu,R.id.location,canAccessLocation());
    TestMenu(menu,R.id.camera  ,canAccessCamera());
    TestMenu(menu,R.id.internet,canAccessInternet());
    TestMenu(menu,R.id.contacts,canAccessContacts());
    TestMenu(menu,R.id.storage ,canAccessStore());
    TestMenu(menu,R.id.extra   ,canAccessExtra());
    TestMenu(menu,R.id.phone1  ,canAccessPhone1());
    TestMenu(menu,R.id.phone2  ,canAccessPhone2());
    TestMenu(menu,R.id.sms     ,canAccessSMS());
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.location:
        if (canAccessLocation()) { doLocationThing(); }
        else{ requestPermissions(LOCATION_PERMS, LOCATION_REQUEST); }
        return(true);

      case R.id.camera:
        if (canAccessCamera()) { doCameraThing();        }
        else { requestPermissions(CAMERA_PERMS, CAMERA_REQUEST);     }
        return(true);

      case R.id.internet:
        if (canAccessInternet()) { doInternetThing(); }
        else{ requestPermissions(INTERNET_PERMS, INTERNET_REQUEST); }
        return(true);

      case R.id.contacts:
        if (canAccessContacts()) { doContactsThing();        }
        else { requestPermissions(CONTACTS_PERMS, CONTACTS_REQUEST); }
        return(true);

      case R.id.storage:
        if (canAccessStore()) { doStoreThing(); }
        else{ requestPermissions(STORAGE_PERMS, STORAGE_REQUEST); }
        return(true);

      case R.id.extra:
        if (canAccessExtra()) { doExtraThing(); }
        else{ requestPermissions(EXTRA_PERMS, EXTRA_REQUEST); }
        return(true);

      case R.id.phone1:
        if (canAccessPhone1()) { doPhone1Thing(); }
        else { requestPermissions(PHONE1_PERMS, PHONE1_REQUEST); }
        return(true);
      case R.id.phone2:
        if (canAccessPhone2()) { doPhone2Thing(); }
        else { requestPermissions(PHONE2_PERMS, PHONE2_REQUEST); }

      case R.id.sms:
        if (canAccessSMS()) {
          doSMSThing();
        }
        else {
          requestPermissions(SMS_PERMS, SMS_REQUEST);
        }
        return(true);

    }

    return(super.onOptionsItemSelected(item));
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    updateTable();

    switch(requestCode) {
      case LOCATION_REQUEST:
        if (canAccessLocation()) { doLocationThing();  return;      }
        break;
      case CAMERA_REQUEST:
        if (canAccessCamera())   { doCameraThing();   return;     }
        break;
      case INTERNET_REQUEST:
        if (canAccessInternet()) { doInternetThing();  return;      }
        break;
      case CONTACTS_REQUEST:
        if (canAccessContacts()) { doContactsThing();  return;      }
        break;


      case STORAGE_REQUEST:
        if (canAccessStore())    { doStoreThing();  return;      }
        break;
      case EXTRA_REQUEST:
        if (canAccessExtra())    { doExtraThing();  return;      }
        break;
      case PHONE1_REQUEST:
        if (canAccessPhone1())   { doPhone1Thing(); return;}
        break;
      case PHONE2_REQUEST:
        if (canAccessPhone2())   { doPhone2Thing();   return;     }

        break;
      case SMS_REQUEST:
        if (canAccessSMS())      { doSMSThing();  return;      }
        break;
    }
    bzzzt();
  }


  private void updateTable() {
    location.setText(String.valueOf(canAccessLocation()));
    camera.setText(  String.valueOf(canAccessCamera()));
    internet.setText(String.valueOf(canAccessInternet()));
    contacts.setText(String.valueOf(canAccessContacts()));
    storage.setText( String.valueOf(canAccessStore()));
    extra.setText(   String.valueOf(canAccessExtra()));
    phone1.setText(  String.valueOf(canAccessPhone1()));
    phone2.setText(  String.valueOf(canAccessPhone2()));
    sms.setText(     String.valueOf(canAccessSMS()));
  }

  private boolean canAccessLocation() {
    return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
  }
  private boolean canAccessCamera() {
    return(hasPermission(Manifest.permission.CAMERA));
  }
  private boolean canAccessInternet() {
    return(hasPermission(Manifest.permission.INTERNET));
  }
  private boolean canAccessContacts() {
    return(hasPermission(Manifest.permission.READ_CONTACTS));
  }
  private boolean canAccessStore() {
    return(hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
  }
  private boolean canAccessExtra() {
    return(hasPermission(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS));
  }
  private boolean canAccessPhone1() {
    return(hasPermission(Manifest.permission.CALL_PHONE));
  }
  private boolean canAccessPhone2() {
    return(hasPermission(Manifest.permission.READ_PHONE_STATE));
  }
  private boolean canAccessSMS() {
    return(hasPermission(Manifest.permission.READ_SMS));
  }

  private boolean hasPermission(String perm) {
    return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
  }

  private void bzzzt() {
    Toast.makeText(this, R.string.toast_bzzzt, Toast.LENGTH_LONG).show();
  }

  private void doLocationThing() {
    Toast.makeText(this, R.string.toast_location, Toast.LENGTH_SHORT).show();
  }
  private void doCameraThing() {
    Toast.makeText(this, R.string.toast_camera, Toast.LENGTH_SHORT).show();
  }
  private void doContactsThing() {
    Toast.makeText(this, R.string.toast_contacts, Toast.LENGTH_SHORT).show();
  }
  private void doInternetThing() {
    Toast.makeText(this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
  }

  private void doStoreThing() {
    Toast.makeText(this, R.string.toast_storage, Toast.LENGTH_SHORT).show();
  }
  private void doExtraThing() {
    Toast.makeText(this, R.string.toast_extra, Toast.LENGTH_SHORT).show();
  }

  private void doPhone1Thing() {
    Toast.makeText(this, R.string.toast_phone1, Toast.LENGTH_SHORT).show();
  }
  private void doPhone2Thing() {
    Toast.makeText(this, R.string.toast_phone2, Toast.LENGTH_SHORT).show();
  }
  private void doSMSThing() {
    Toast.makeText(this, R.string.toast_sms, Toast.LENGTH_SHORT).show();
  }

}
