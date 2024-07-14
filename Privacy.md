## Spark Music Player: Privacy policy

Welcome to the Spark Music Player app for Android!

This is an open-source Android app developed by Theekshana Nirmana. The source code is available on GitHub under the Apache-2.0 license.

As an avid Android user myself, I take privacy very seriously.
I know how frustrating it is when apps collect your data without your knowledge.

### Data collected by the app

I hereby state, to the best of my knowledge and belief, that I have not programmed this app to collect any personally identifiable information. All data is stored locally in your device only, and can be simply erased by clearing the app's data or uninstalling it.

### Explanation of permissions requested in the app

The list of permissions required by the app can be found in the `AndroidManifest.xml` file:
<br/>

| Permission | Why it is required |
| :---: | --- |
| `android.permission.READ_MEDIA_AUDIO` (API level >= 33) and `android.permission.READ_EXTERNAL_STORAGE` | Has to be granted by the user manually; can be revoked by the system or the user at any time. This is required to collect locally stored mp3 files. |
| `android.permission.POST_NOTIFICATIONS` | Required by the app to post notifications. Has to be granted by the user manually; can be revoked by the system or the user at any time. It is highly recommended that you allow this permission so that the app can show the playback notification. |
| `android.permission.INTERNET` and `android.permission.ACCESS_NETWORK_STATE` | Required to fetch artist images, profile details, popularity, and many other related information. |
| `android.permission.FOREGROUND_SERVICE` and `android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK` | Enables the app to create foreground services that will play music playback. Permission automatically granted by the system; can't be revoked by user. |

 <hr style="border:1px solid gray">

If you find any security vulnerability that has been inadvertently caused by me, or have any question regarding how the app protectes your privacy, please post a discussion on GitHub, and I will surely try to fix it/help you.

Yours sincerely,  
Theekshana Nirmana
