package orsac.rosmerta.orsac_vehicle.android.Orsac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import customfonts.MyTextView;
import orsac.rosmerta.orsac_vehicle.android.R;

public class Orsac_About_us extends AppCompatActivity {
    private MyTextView tool_title;
    private ImageView tool_back_icon;

    private Toolbar tb;
    private TextView termsConditions;
    private WebView terms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        tool_title = (MyTextView) findViewById(R.id.tool_title);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onbackClick();
            }
        });
tool_title.setText("About Us");
        String htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        String mydata_test = "<p><b>At A Glance:</b> Odisha Space Applications Centre (ORSAC), the apex body of the State of Odisha for\n" +
                "space technology applications, was established in the year 1984. The Centre is located at Bhubaneswar in its own building at Chandrasekharpur.\n" +
                 "The Centre is equipped with sophisticated GIS & Computer laboratories with latest GIS and Image softwares. \n" +
                "It has a team of well-experienced multidisciplinary application scientists to undertake Remote Sensing, GIS & GPS based projects. \n" +
                "In the field of communication technology application, the centre runs GRAMSAT & EDUSAT programme. GRAMSAT includes interactive training programme,\n" +
                "production & telecast of developmental programmes in fiction based drama format, documentary format as well as news magazine format.n" +
                " EDUSAT includes telecast of live class room programmes and enrichment programmes for the benefit of High School students. </p><br>\n" +
                "<p>Remotely Sensed data from Earth Observation Satellites,in conjunction with field observations and collateral data, \n" +
                "facilitates generation of information for decision making in a time and cost effective manner for scientific management of our natural resources,\n" +
                "for monitoring the environment and ecosystems as well as for assessment of damage due to natural disasters.\n" +
                "To give substance to the above capability of the technology and to further the goals of Indian Space Programme,\n" +
                "ORSAC has defined its objectives and relentlessly attempts to satisfy the same in the context of the needs of the state.</p><br>"+
                "<p>Copyright  2017 ORSAC, All rights reserved.</p>";
        terms = (WebView) findViewById(R.id.terms);
        terms.loadData(String.format(htmlText, mydata_test), "text/html", "utf-8");
       /* termsConditions = (TextView) findViewById(R.id.terms);
        termsConditions.setText(Html.fromHtml("<p><b>At A Glance:</b> Odisha Space Applications Centre (ORSAC), the apex body of the State of Odisha for" +
                " space technology applications, was established in the year 1984. The Centre is located at Bhubaneswar in its own building at Chandrasekharpur." +
                " The Centre is equipped with sophisticated GIS & Computer laboratories with latest GIS and Image softwares. " +
                "It has a team of well-experienced multidisciplinary application scientists to undertake Remote Sensing, GIS & GPS based projects. " +
                "In the field of communication technology application, the centre runs GRAMSAT & EDUSAT programme. GRAMSAT includes interactive training programme, " +
                "production & telecast of developmental programmes in fiction based drama format, documentary format as well as news magazine format." +
                " EDUSAT includes telecast of live class room programmes and enrichment programmes for the benefit of High School students. </p><br>" +

                "<p>Remotely Sensed data from Earth Observation Satellites,in conjunction with field observations and collateral data, " +
                "facilitates generation of information for decision making in a time and cost effective manner for scientific management of our natural resources," +
                " for monitoring the environment and ecosystems as well as for assessment of damage due to natural disasters." +
                " To give substance to the above capability of the technology and to further the goals of Indian Space Programme," +
                " ORSAC has defined its objectives and relentlessly attempts to satisfy the same in the context of the needs of the state.</p><br>" +

              *//*  "<p><b>The objectives are : </b> The App is made available for your own, personal use. The App must not be used for any commercial purpose whatsoever or for any illegal or unauthorised purpose. When you use the App you must comply with all applicable laws in India and with any applicable international laws, including the local laws in your country of residence (together referred to as \"Applicable Laws\").<p><br>" +
                "<p>You agree that when using the App you will comply with all Applicable Laws and these Terms. In particular, but without limitation, you agree not to:</p><br>" +
                "<p>(a) Use the App in any unlawful manner or in a manner which promotes or encourages illegal activity including (without limitation) copyright infringement; or</p><br>" +
                "<p>(b) Attempt to gain unauthorised access to the App or any networks, servers or computer systems connected to the App; or</p><br>" +
                "<p>(c) Modify, adapt, translate or reverse engineer any part of the App or re-format or frame any portion of the pages comprising the App, save to the extent expressly permitted by these Terms or by law.</p><br>" +
                "<p>You agree to indemnify Tatpar in full and on demand from and against any loss, damage, costs or expenses which they suffer or incur directly or indirectly as a result of your use of the App otherwise than in accordance with these Terms or Applicable Laws.</p><br>" +
                "<p><b>CONTENT:</b> The copyright of all material contained on, in, or available through the App including all information, data, text, music, sound, photographs, graphics and video messages, the selection and arrangement thereof, and all source code, software compilations and other material (\"Material\") is owned by or licensed to ORSAC, unless explicitly acknowledged to have been taken from any published source. All rights are reserved. You can view, print or download extracts of the Material for your own personal use but you cannot otherwise copy, edit, vary, reproduce, publish, display, distribute, store, transmit, commercially exploit, disseminate in any form whatsoever or use the Material without ORSAC\'s express permission.<p></br>" +
                "<p>The trademarks, service marks, and logos (\"Trade Marks\") contained on or in the App are owned by Rackloot or third party partners of Rackloot. You cannot use, copy, edit, vary, reproduce, publish, display, distribute, store, transmit, commercially exploit or disseminate the Trade Marks without the prior written consent of Rackloot.</p><br>" +
                "<p><b>LINK TO THIRD PARTIES:</b> The App may contain links to websites operated by third parties (\"Third Party Websites\").Rackloot may monetise some of these links through the use of third party affiliate programmes. Notwithstanding such affiliate programmes, Rackloot does not have any influence or control over any such Third Party Websites or mobile applications or physical business operations, and, unless otherwise stated, is not responsible for and does not endorse any Third Party websites or applications or operations or their availability or contents.</p><br>" +
                "<p><b>PRIVACY POLICY:</b> Rackloot provides this Privacy Policy to inform users of our policies and procedures regarding the collection, use and disclosure of personally identifiable information received from users of this application (\"App\") or website (the \"Website\") and all other products and services owned, controlled or offered by Rackloot, and all content offered as a part thereof (the \"Services\"). This Privacy Policy may be updated from time to time for any reason, at our sole discretion. We will notify you of any material changes to our Privacy Policy by posting the new Privacy Policy on our Website. You are advised to consult this Privacy Policy regularly for any changes. By using or accessing the Website & Rackloot Mobile App, you are accepting the practices described in this Privacy Policy, and you are consenting to our processing of your information as set forth in this Privacy Policy now and as amended by us.</p><br>" +
                "<p><b>PERSONAL DATA:</b> We collect personal data from you, the user, when you voluntarily choose to register with Rackloot. We use the personal data that we collect in order to provide and improve our services, and as otherwise set forth in this Privacy Policy and in our Terms of Service. Examples of personal data include but are not limited to first name, last name, address, email address, telephone number, birth date, billing and credit card information.</p><br>" +
                "<p><b>THIRD PARTY PERSONAL DATA:</b>  We may also collect personal data from you, the user, about certain third party individuals. We use this personal data that we collect from you in order to provide and improve our services, facilitate communication between you and such third parties, and as otherwise set forth in this Privacy Policy and in our Terms of Service. You warrant to Rackloot that any personal data you provide us about any third party individuals was obtained by you with full consent from such third party, and that such third party has not communicated to you that they wish to opt out of receiving communication from Rackloot.</p><br>" +
                "<p><b>USE OF PERSONAL DATA:</b>  Rackloot uses the personal data you provide in a manner that is consistent with this Privacy Policy or with any applicable service-specific agreement. If you provide personal data for a certain reason, we may use the personal data in connection with the reason for which it was provided. Rackloot and its affiliates also use your personal data and other personally non-identifiable information collected through the Website/Mobile App to help us improve the content and functionality of the Website, to better understand the users of our Website, and to improve our services.</p><br>" +
                "<p><b>CUSTOMER CREDIT/DEBIT CARD INFORMATION:</b> Rackloot uses a third party to keep a protected copy of your credit/Debit card number. This billing data belongs to you and by utilizing the Service, you grant Rackloot a license to use this data to bill you for services rendered.</p><br>" +
                "<p><b>PERSONALLY NON-IDENTIFIABLE INFORMATION:</b>  We may collect personally non-identifiable information from you at the time of registration, or when you choose to use certain of our services. This information is not, by itself, sufficient to identify or contact you.Rackloot may store such information, or it may be included in databases owned and maintained by Rackloot\'s affiliates, agents or service providers. This Mobile App or Website may use such information and pool it with other information to provide better services to you.</p><br>" +
                "<p>When you interact with us through the Website/Mobile App, we receive and store certain additional personally non-identifiable information. Such information, which is collected passively using various technologies, cannot presently be used to specifically identify you. Examples include IP addresses, browser types, domain names, and other anonymous statistical data involving your use of the Website and/or our services.</p><br>" +
                "<p><b>AGGREGATED PERSONAL DATA:</b> In an ongoing effort to better understand our users, we might analyze the personal data that is provided to the Website/Mobile App in aggregate form. We may share this aggregate data with our affiliates, agents and business partners. We may also disclose aggregated user statistics in order to describe our services to current and prospective business partners and to other third parties for other lawful purposes.</p><br>" +
                "<p><b>SHARING INFORMATION:</b> We won\'t share your personal information with anyone except for the situations listed below or unless we specifically inform you and give you an opportunity to opt out of our sharing your personal information.</p><br>" +
                "<p>We may share your personal information with:</p><br>" +
                "<p>- <i>Authorized service providers.</i>  Our authorized service providers are companies that perform certain services on our behalf. These services may include but are not limited to fulfilling orders, processing credit card payments, delivering packages, providing customer service and marketing assistance, performing business and sales analyses, supporting the Website functionality, and supporting contests, sweepstakes, surveys and other features we offer. These service providers may have access to your personal information to the extent necessary to perform their functions, but we do not allow them to share or use any of your personal information for any other purpose.</p><br>" +
                "<p>- <i>Rackloot affiliates.</i> We may share information we collect, including personal information, with Rackloot-owned or affiliated businesses. Sharing information with our affiliates enables us to provide you with information about a variety of products and services that might interest you.</p><br>" +
                "<p>- <i>Other Situations.</i> We also may disclose your information:</p><br>" +
                "<p>In response to a subpoena or similar investigative demand, a court order, or a request for cooperation from law enforcement or other government agency; to establish or exercise our legal rights; to defend against legal claims; or as otherwise required by law. In such cases, we may raise or waive any legal objection or right available to us.</p><br>" +
                "<p>When we believe disclosure is appropriate in connection with efforts to investigate, prevent, or take other action regarding illegal activity, suspected fraud or other wrongdoing; to protect and defend the rights, property or safety of our company, our users, our employees, or others; to comply with applicable law or cooperate with law enforcement; or to enforce the Website\'s Terms of Use or other agreements or policies.</p><br>" +
                "<p>In connection with a substantial corporate transaction, such as the sale of our business, a divestiture, merger, consolidation, or asset sale, or in the unlikely event of bankruptcy.</p><br>" +
                "<p><b>SECURITY:</b> We take precautions to ensure the security of your personal information. However, we cannot guarantee that hackers or unauthorized personnel will not gain access to your personal information despite our efforts. You should note that in using the Website and our related services, this information will travel through third party infrastructures which are not under our control. We cannot protect, nor does this Privacy Policy apply to, any information that you transmit to other users of the Website or App. You should never transmit personal or identifying information to other users.</p><br>" +
                "<p><b>OPT-OUT POLICY:</b> If, at any time after registering, you change your mind about receiving information from us or about the use of information volunteered by you, please send us a request specifying your new choice. Please contact us as specified above. We shall not search, save, track or follow the data that you generate online/ any activity that you do online. But we shall keep your data that you agreed to share with us during the life span of this contract.</p><br>" +
                "<p><b>CHANGES TO PRIVACY POLICY:</b> Although most changes are likely to be minor, Rackloot may change its Privacy Policy from time to time, and in Rackloot’s sole discretion. rackloot encourages visitors to frequently check this page for any changes to its Privacy Policy. Your continued use of the Services after any change in this Privacy Policy will constitute your acceptance of such change.</p><br>" +
                "<p><b>DISCLAIMER / LIABILITY:</b> USE OF THE APP IS AT YOUR OWN RISK. THE APP IS PROVIDED ON AN \"AS IS\" BASIS. TO THE MAXIMUM EXTENT PERMITTED BY LAW: (A) RACKLOOT DISCLAIMS ALL LIABILITY WHATSOEVER, WHETHER ARISING IN CONTRACT, TORT (INCLUDING NEGLIGENCE) OR OTHERWISE IN RELATION TO THE APP; AND (B) ALL IMPLIED WARRANTIES, TERMS AND CONDITIONS RELATING TO THE APP (WHETHER IMPLIED BY STATUE, COMMON LAW OR OTHERWISE), INCLUDING (WITHOUT LIMITATION) ANY WARRANTY, TERM OR CONDITION AS TO ACCURACY, COMPLETENESS, SATISFACTORY QUALITY, PERFORMANCE, FITNESS FOR PURPOSE OR ANY SPECIAL PURPOSE, AVAILABILITY, NON INFRINGEMENT, INFORMATION ACCURACY, INTEROPERABILITY, QUIET ENJOYMENT AND TITLE ARE, AS BETWEEN RACKLOOT AND YOU, HEREBY EXCLUDED. IN PARTICULAR, BUT WITHOUT PREJUDICE TO THE FOREGOING, WE ACCEPT NO RESPONSIBILITY FOR ANY TECHNICAL FAILURE OF THE INTERNET AND/OR THE APP; OR ANY DAMAGE OR INJURY TO USERS OR THEIR EQUIPMENT AS A RESULT OF OR RELATING TO THEIR USE OF THE APP. YOUR STATUTORY RIGHTS ARE NOT AFFECTED.</p><br>" +
                "<p>Rackloot will not be liable, in contract, tort (including, without limitation, negligence), under statute or otherwise, as a result of or in connection with the App, for any: (i) economic loss (including, without limitation, loss of revenues, profits, contracts, business or anticipated savings); or (ii) loss of goodwill or reputation; or (iii) special or indirect or consequential loss.</p><br>" +
                "<p><b>SERVICE SUSPENSION:</b> Rackloot reserves the right to suspend or cease providing any services relating to the apps published by it, with or without notice, and shall have no liability or responsibility to you in any manner whatsoever if it chooses to do so.</p><br>" +
                "<p><b>ADVERTISERS IN THE APP:</b> We accept no responsibility for advertisements contained within the App. If you agree to purchase goods and/or services from any third party who advertises in the App, you do so at your own risk. The advertiser, not Rackloot, is responsible for such goods and/or services and if you have any queries or complaints in relation to them, your only recourse is against the advertiser.</p><br>" +
                "<p><b>COMPETITIONS:</b> If you take part in any competition which is conducted by Rackloot or through the App (\"Competition\"), you agree to be bound by the rules of that competition and any other rules specified by Rackloot from time to time (\"Competition Rules\") and by the decisions of Rackloot, which are final in all matters relating to the Competition. Rackloot reserves the right to disqualify any entrant and/or winner in its absolute discretion without notice in accordance with the Competition Rules.</p><br>" +
                "<p><b>IN-APP VOUCHER CODES:</b> Any in-app voucher codes issued by Rackloot may only be used in accordance with our Terms and Conditions for in-app voucher codes.</p><br>" +
                "<p><b>GENERAL:</b> These Terms (as amended from time to time) constitute the entire agreement between you and Rackloot concerning your use of the App.</p><br>" +
               *//* "<p>Copyright © 2017 Orsac, All rights reserved.</p>"));*/
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //onBackPressed();
                Intent inty = new Intent(Orsac_About_us.this,NavigationActivity.class);
                startActivity(inty);
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    protected void onbackClick()
    {
        Intent myIntent = new Intent();
        setResult(RESULT_OK,myIntent);
        super.finish();
    }

}
