package com.nsa.comuty.onboarding.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsa.comuty.R;
import com.nsa.comuty.databinding.CountryCodeItemBinding;
import com.nsa.comuty.databinding.ScreensLayoutBinding;
import com.nsa.comuty.onboarding.interfaces.CountryCodeClickListener;
import com.nsa.comuty.onboarding.models.CountryModel;
import com.nsa.comuty.onboarding.models.ScreensModel;

import java.util.ArrayList;
import java.util.List;

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.ViewHolder> {

    private Context context;
    private List<CountryModel> list,searchList;
    private CountryCodeClickListener listener;

    public CountryCodeAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
        searchList=new ArrayList<>();
     setData();

    }

    public void setListener(CountryCodeClickListener listener) {
        this.listener = listener;
    }

    public List<CountryModel> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<CountryModel> searchList) {
        this.searchList = searchList;
    }

    public List<CountryModel> getList() {
        return list;
    }

    public void setList(List<CountryModel> list) {
        this.list = list;
    }

    private void setData() {
        list.clear();
        list.add(new CountryModel("af", "Afghanistan", "+93", "🇦🇫"));
        list.add(new CountryModel("al", "Albania", "+355", "🇦🇱"));
        list.add(new CountryModel("dz", "Algeria", "+213", "🇩🇿"));
        list.add(new CountryModel("as", "American Samoa", "+1684", "🇦🇸"));
        list.add(new CountryModel("ad", "Andorra", "+376", "🇦🇩"));
        list.add(new CountryModel("ao", "Angola", "+244", "🇦🇴"));
        list.add(new CountryModel("ai", "Anguilla", "+1264", "🇦🇮"));
        list.add(new CountryModel("aq", "Antarctica", "+672", "🇦🇶"));
        list.add(new CountryModel("ag", "Antigua and Barbuda", "+1268", "🇦🇬"));
        list.add(new CountryModel("ar", "Argentina", "+54", "🇦🇷"));
        list.add(new CountryModel("am", "Armenia", "+374", "🇦🇲"));
        list.add(new CountryModel("aw", "Aruba", "+297", "🇦🇼"));
        list.add(new CountryModel("au", "Australia", "+61", "🇦🇺"));
        list.add(new CountryModel("at", "Austria", "+43", "🇦🇹"));
        list.add(new CountryModel("az", "Azerbaijan", "+994", "🇦🇿"));
        list.add(new CountryModel("bs", "Bahamas", "+1242", "🇧🇸"));
        list.add(new CountryModel("bh", "Bahrain", "+973", "🇧🇭"));
        list.add(new CountryModel("bd", "Bangladesh", "+880", "🇧🇩"));
        list.add(new CountryModel("bb", "Barbados", "+1242", "🇧🇧"));
        list.add(new CountryModel("by", "Belarus", "+375", "🇧🇾"));
        list.add(new CountryModel("be", "Belgium", "+32", "🇧🇪"));
        list.add(new CountryModel("bz", "Belize", "+501", "🇧🇿"));
        list.add(new CountryModel("bj", "Benin", "+229", "🇧🇯"));
        list.add(new CountryModel("bm", "Bermuda", "+1441", "🇧🇲"));
        list.add(new CountryModel("bt", "Bhutan", "+975", "🇧🇹"));
        list.add(new CountryModel("bo", "Bolivia", "+591", "🇧🇴"));
        list.add(new CountryModel("ba", "Bosnia And Herzegovina", "+387", "🇧🇦"));
        list.add(new CountryModel("bw", "Botswana", "+267", "🇧🇼"));
        list.add(new CountryModel("br", "Brazil", "+55", "🇧🇷"));
        list.add(new CountryModel("io", "British Indian Ocean Territory", "+246", "🇮🇴"));
        list.add(new CountryModel("vg", "British Virgin Islands", "+1284", "🇻🇬"));
        list.add(new CountryModel("bn", "Brunei Darussalam", "+673", "🇧🇳"));
        list.add(new CountryModel("bg", "Bulgaria", "+359", "🇧🇬"));
        list.add(new CountryModel("bf", "Burkina Faso", "+226", "🇧🇫"));
        list.add(new CountryModel("bi", "Burundi", "+257", "🇧🇮"));
        list.add(new CountryModel("kh", "Cambodia", "+855", "🇰🇭"));
        list.add(new CountryModel("cm", "Cameroon", "+237", "🇨🇲"));
        list.add(new CountryModel("ca", "Canada", "+1", "🇨🇦"));
        list.add(new CountryModel("cv", "Cape Verde", "+238", "🇨🇻"));
        list.add(new CountryModel("ky", "Cayman Islands", "+345", "🇰🇾"));
        list.add(new CountryModel("cf", "Central African Republic", "+236", "🇨🇫"));
        list.add(new CountryModel("td", "Chad", "+235", "🇹🇩"));
        list.add(new CountryModel("cl", "Chile", "+56", "🇨🇱"));
        list.add(new CountryModel("cn", "China", "+86", "🇨🇳"));
        list.add(new CountryModel("cx", "Christmas Island", "+61", "🇨🇽"));
        list.add(new CountryModel("cc", "Cocos (keeling) Islands", "+61", "🇨🇨"));
        list.add(new CountryModel("co", "Colombia", "+57", "🇨🇴"));
        list.add(new CountryModel("km", "Comoros", "+269", "🇰🇲"));
        list.add(new CountryModel("ck", "Cook Islands", "+682", "🇨🇰"));
        list.add(new CountryModel("cr", "Costa Rica", "+506", "🇨🇷"));
        list.add(new CountryModel("hr", "Croatia", "+385", "🇭🇷"));
        list.add(new CountryModel("cu", "Cuba", "+53", "🇨🇺"));
        list.add(new CountryModel("cy", "Cyprus", "+357", "🇨🇾"));
        list.add(new CountryModel("cz", "Czech Republic", "+420", "🇨🇿"));
        list.add(new CountryModel("ci", "Côte D'ivoire", "+225", "🇨🇮"));
        list.add(new CountryModel("cd", "Democratic Republic of the Congo", "+243", "🇨🇩"));
        list.add(new CountryModel("dk", "Denmark", "+45", "🇩🇰"));
        list.add(new CountryModel("dj", "Djibouti", "+253", "🇩🇯"));
        list.add(new CountryModel("dm", "Dominica", "+1767", "🇩🇲"));
        list.add(new CountryModel("do", "Dominican Republic", "+1849", "🇩🇴"));
        list.add(new CountryModel("ec", "Ecuador", "+593", "🇪🇨"));
        list.add(new CountryModel("eg", "Egypt", "+20", "🇪🇬"));
        list.add(new CountryModel("sv", "El Salvador", "+503", "🇸🇻"));
        list.add(new CountryModel("gq", "Equatorial Guinea", "+240", "🇬🇶"));
        list.add(new CountryModel("er", "Eritrea", "+291", "🇪🇷"));
        list.add(new CountryModel("ee", "Estonia", "+372", "🇪🇪"));
        list.add(new CountryModel("et", "Ethiopia", "+251", "🇪🇹"));
        list.add(new CountryModel("fk", "Falkland Islands (malvinas)", "+500", "🇫🇰"));
        list.add(new CountryModel("fo", "Faroe Islands", "+298", "🇫🇴"));
        list.add(new CountryModel("fj", "Fiji", "+679", "🇫🇯"));
        list.add(new CountryModel("fi", "Finland", "+358", "🇫🇮"));
        list.add(new CountryModel("fr", "France", "+33", "🇫🇷"));
        list.add(new CountryModel("gf", "French Guiana", "+594", "🇬🇫"));
        list.add(new CountryModel("pf", "French Polynesia", "+689", "🇵🇫"));
        list.add(new CountryModel("ga", "Gabon", "+241", "🇬🇦"));
        list.add(new CountryModel("gm", "Gambia", "+220", "🇬🇲"));
        list.add(new CountryModel("ge", "Georgia", "+995", "🇬🇪"));
        list.add(new CountryModel("de", "Germany", "+49", "🇩🇪"));
        list.add(new CountryModel("gh", "Ghana", "+233", "🇬🇭"));
        list.add(new CountryModel("gi", "Gibraltar", "+350", "🇬🇮"));
        list.add(new CountryModel("gr", "Greece", "+30", "🇬🇷"));
        list.add(new CountryModel("gl", "Greenland", "+299", "🇬🇱"));
        list.add(new CountryModel("gd", "Grenada", "+1473", "🇬🇩"));
        list.add(new CountryModel("gp", "Guadeloupe", "+590", "🇬🇵"));
        list.add(new CountryModel("gu", "Guam", "+1671", "🇬🇺"));
        list.add(new CountryModel("gt", "Guatemala", "+502", "🇬🇹"));
        list.add(new CountryModel("gg", "Guernsey", "+44", "🇬🇬"));
        list.add(new CountryModel("gn", "Guinea", "+224", "🇬🇳"));
        list.add(new CountryModel("gw", "Guinea-Bissau", "+245", "🇬🇼"));
        list.add(new CountryModel("gy", "Guyana", "+592", "🇬🇾"));
        list.add(new CountryModel("ht", "Haiti", "+509", "🇭🇹"));
        list.add(new CountryModel("va", "Holy See (Vatican City State)", "+379", "🇻🇦"));
        list.add(new CountryModel("hn", "Honduras", "+504", "🇭🇳"));
        list.add(new CountryModel("hk", "Hong Kong", "+852", "🇭🇰"));
        list.add(new CountryModel("hu", "Hungary", "+36", "🇭🇺"));
        list.add(new CountryModel("is", "Iceland", "+354", "🇮🇸"));
        list.add(new CountryModel("in", "India", "+91", "🇮🇳"));
        list.add(new CountryModel("id", "Indonesia", "+62", "🇮🇩"));
        list.add(new CountryModel("ir", "Iran", "+98", "🇮🇷"));
        list.add(new CountryModel("iq", "Iraq", "+964", "🇮🇶"));
        list.add(new CountryModel("ie", "Ireland", "+353", "🇮🇪"));
        list.add(new CountryModel("im", "Isle Of Man", "+44", "🇮🇲"));
        list.add(new CountryModel("il", "Israel", "+972", "🇮🇱"));
        list.add(new CountryModel("it", "Italy", "+39", "🇮🇹"));
        list.add(new CountryModel("jm", "Jamaica", "+1876", "🇯🇲"));
        list.add(new CountryModel("jp", "Japan", "+81", "🇯🇵"));
        list.add(new CountryModel("je", "Jersey", "+44", "🇯🇪"));
        list.add(new CountryModel("jo", "Jordan", "+962", "🇯🇴"));
        list.add(new CountryModel("kz", "Kazakhstan", "+7", "🇰🇿"));
        list.add(new CountryModel("ke", "Kenya", "+254", "🇰🇪"));
        list.add(new CountryModel("ki", "Kiribati", "+686", "🇰🇮"));
        list.add(new CountryModel("xk", "Kosovo", "+383", "🇽🇰"));
        list.add(new CountryModel("kw", "Kuwait", "+965", "🇰🇼"));
        list.add(new CountryModel("kg", "Kyrgyzstan", "+996", "🇰🇬"));
        list.add(new CountryModel("la", "Lao People's Democratic Republic", "+856", "🇱🇦"));
        list.add(new CountryModel("lv", "Latvia", "+371", "🇱🇻"));
        list.add(new CountryModel("lb", "Lebanon", "+961", "🇱🇧"));
        list.add(new CountryModel("ls", "Lesotho", "+266", "🇱🇸"));
        list.add(new CountryModel("lr", "Liberia", "+231", "🇱🇷"));
        list.add(new CountryModel("ly", "Libya", "+218", "🇱🇾"));
        list.add(new CountryModel("li", "Liechtenstein", "+423", "🇱🇮"));
        list.add(new CountryModel("lt", "Lithuania", "+370", "🇱🇹"));
        list.add(new CountryModel("lu", "Luxembourg", "+352", "🇱🇺"));
        list.add(new CountryModel("mo", "Macao Sar China", "+853", "🇲🇴"));
        list.add(new CountryModel("mk", "Macedonia", "+389", "🇲🇰"));
        list.add(new CountryModel("mg", "Madagascar", "+261", "🇲🇬"));
        list.add(new CountryModel("mw", "Malawi", "+265", "🇲🇼"));
        list.add(new CountryModel("my", "Malaysia", "+60", "🇲🇾"));
        list.add(new CountryModel("mv", "Maldives", "+960", "🇲🇻"));
        list.add(new CountryModel("ml", "Mali", "+223", "🇲🇱"));
        list.add(new CountryModel("mt", "Malta", "+356", "🇲🇹"));
        list.add(new CountryModel("mh", "Marshall Islands", "+692", "🇲🇭"));
        list.add(new CountryModel("mq", "Martinique", "+596", "🇲🇶"));
        list.add(new CountryModel("mr", "Mauritania", "+222", "🇲🇷"));
        list.add(new CountryModel("mu", "Mauritius", "+230", "🇲🇺"));
        list.add(new CountryModel("yt", "Mayotte", "+262", "🇾🇹"));
        list.add(new CountryModel("mx", "Mexico", "+52", "🇲🇽"));
        list.add(new CountryModel("fm", "Micronesia", "+691", "🇫🇲"));
        list.add(new CountryModel("md", "Moldova", "+373", "🇲🇩"));
        list.add(new CountryModel("mc", "Monaco", "+377", "🇲🇨"));
        list.add(new CountryModel("mn", "Mongolia", "+976", "🇲🇳"));
        list.add(new CountryModel("me", "Montenegro", "+382", "🇲🇪"));
        list.add(new CountryModel("ms", "Montserrat", "+1664", "🇲🇸"));
        list.add(new CountryModel("ma", "Morocco", "+212", "🇲🇦"));
        list.add(new CountryModel("mz", "Mozambique", "+258", "🇲🇿"));
        list.add(new CountryModel("mm", "Myanmar (Burma)", "+95", "🇲🇲"));
        list.add(new CountryModel("na", "Namibia", "+264", "🇳🇦"));
        list.add(new CountryModel("nr", "Nauru", "+674", "🇳🇷"));
        list.add(new CountryModel("np", "Nepal", "+977", "🇳🇵"));
        list.add(new CountryModel("nl", "Netherlands", "+31", "🇳🇱"));
        list.add(new CountryModel("nc", "New Caledonia", "+687", "🇳🇨"));
        list.add(new CountryModel("nz", "New Zealand", "+64", "🇳🇿"));
        list.add(new CountryModel("ni", "Nicaragua", "+505", "🇳🇮"));
        list.add(new CountryModel("ne", "Niger", "+227", "🇳🇪"));
        list.add(new CountryModel("ng", "Nigeria", "+234", "🇳🇬"));
        list.add(new CountryModel("nu", "Niue", "+683", "🇳🇺"));
        list.add(new CountryModel("nf", "Norfolk Island", "+1670", "🇳🇫"));
        list.add(new CountryModel("kp", "North Korea", "+672", "🇰🇵"));
        list.add(new CountryModel("mp", "Northern Mariana Islands", "+850", "🇲🇵"));
        list.add(new CountryModel("no", "Norway", "+47", "🇳🇴"));
        list.add(new CountryModel("om", "Oman", "+968", "🇴🇲"));
        list.add(new CountryModel("pk", "Pakistan", "+92", "🇵🇰"));
        list.add(new CountryModel("pw", "Palau", "+680", "🇵🇼"));
        list.add(new CountryModel("ps", "Palestinian Territory, Occupied", "+970", "🇵🇸"));
        list.add(new CountryModel("pa", "Panama", "+507", "🇵🇦"));
        list.add(new CountryModel("pg", "Papua New Guinea", "+675", "🇵🇬"));
        list.add(new CountryModel("py", "Paraguay", "+595", "🇵🇾"));
        list.add(new CountryModel("pe", "Peru", "+51", "🇵🇪"));
        list.add(new CountryModel("ph", "Philippines", "+63", "🇵🇭"));
        list.add(new CountryModel("pn", "Pitcairn Islands", "+870", "🇵🇳"));
        list.add(new CountryModel("pl", "Poland", "+48", "🇵🇱"));
        list.add(new CountryModel("pt", "Portugal", "+351", "🇵🇹"));
        list.add(new CountryModel("pr", "Puerto Rico", "+1939", "🇵🇷"));
        list.add(new CountryModel("qa", "Qatar", "+974", "🇶🇦"));
        list.add(new CountryModel("cg", "Republic of the Congo - Brazzaville", "+242", "🇨🇬"));
        list.add(new CountryModel("ro", "Romania", "+40", "🇷🇴"));
        list.add(new CountryModel("ru", "Russian Federation", "+7", "🇷🇺"));
        list.add(new CountryModel("rw", "Rwanda", "+250", "🇷🇼"));
        list.add(new CountryModel("re", "Réunion", "+262", "🇷🇪"));
        list.add(new CountryModel("bl", "Saint Barthélemy", "+590", "🇧🇱"));
        list.add(new CountryModel("sh", "Saint Helena", "+290", "🇸🇭"));
        list.add(new CountryModel("kn", "Saint Kitts & Nevis", "+1869", "🇰🇳"));
        list.add(new CountryModel("lc", "Saint Lucia", "+1758", "🇱🇨"));
        list.add(new CountryModel("mf", "Saint Martin", "+590", "🇲🇫"));
        list.add(new CountryModel("pm", "Saint Pierre & Miquelon", "+508", "🇵🇲"));
        list.add(new CountryModel("vc", "Saint Vincent & The Grenadines", "+1784", "🇻🇨"));
        list.add(new CountryModel("ws", "Samoa", "+685", "🇼🇸"));
        list.add(new CountryModel("sm", "San Marino", "+378", "🇸🇲"));
        list.add(new CountryModel("st", "Sao Tome & Principe", "+239", "🇸🇹"));
        list.add(new CountryModel("sa", "Saudi Arabia", "+966", "🇸🇦"));
        list.add(new CountryModel("sn", "Senegal", "+221", "🇸🇳"));
        list.add(new CountryModel("rs", "Serbia", "+381", "🇷🇸"));
        list.add(new CountryModel("sc", "Seychelles", "+248", "🇸🇨"));
        list.add(new CountryModel("sl", "Sierra Leone", "+232", "🇸🇱"));
        list.add(new CountryModel("sg", "Singapore", "+65", "🇸🇬"));
        list.add(new CountryModel("sx", "Sint Maarten", "+1", "🇸🇽"));
        list.add(new CountryModel("sk", "Slovakia", "+421", "🇸🇰"));
        list.add(new CountryModel("si", "Slovenia", "+386", "🇸🇮"));
        list.add(new CountryModel("sb", "Solomon Islands", "+677", "🇸🇧"));
        list.add(new CountryModel("so", "Somalia", "+252", "🇸🇴"));
        list.add(new CountryModel("za", "South Africa", "+27", "🇿🇦"));
        list.add(new CountryModel("gs", "South Africa (South Georgia & South Sandwich Islands)", "+500", "🇬🇸"));
        list.add(new CountryModel("kr", "South Korea", "+82", "🇰🇷"));
        list.add(new CountryModel("ss", "South Sudan", "+211", "🇸🇸"));
        list.add(new CountryModel("es", "Spain", "+34", "🇪🇸"));
        list.add(new CountryModel("lk", "Sri Lanka", "+94", "🇱🇰"));
        list.add(new CountryModel("sd", "Sudan", "+249", "🇸🇩"));
        list.add(new CountryModel("sr", "Suriname", "+597", "🇸🇷"));
        list.add(new CountryModel("sz", "Swaziland", "+268", "🇸🇿"));
        list.add(new CountryModel("se", "Sweden", "+46", "🇸🇪"));
        list.add(new CountryModel("ch", "Switzerland", "+41", "🇨🇭"));
        list.add(new CountryModel("sy", "Syrian Arab Republic", "+963", "🇸🇾"));
        list.add(new CountryModel("tw", "Taiwan", "+886", "🇹🇼"));
        list.add(new CountryModel("tj", "Tajikistan", "+992", "🇹🇯"));
        list.add(new CountryModel("tz", "Tanzania", "+255", "🇹🇿"));
        list.add(new CountryModel("th", "Thailand", "+66", "🇹🇭"));
        list.add(new CountryModel("tl", "Timor-Leste", "+670", "🇹🇱"));
        list.add(new CountryModel("tg", "Togo", "+228", "🇹🇬"));
        list.add(new CountryModel("tk", "Tokelau", "+690", "🇹🇰"));
        list.add(new CountryModel("to", "Tonga", "+676", "🇹🇴"));
        list.add(new CountryModel("tt", "Trinidad & Tobago", "+1868", "🇹🇹"));
        list.add(new CountryModel("tn", "Tunisia", "+216", "🇹🇳"));
        list.add(new CountryModel("tr", "Turkey", "+90", "🇹🇷"));
        list.add(new CountryModel("tm", "Turkmenistan", "+993", "🇹🇲"));
        list.add(new CountryModel("tc", "Turks & Caicos Islands", "+1649", "🇹🇨"));
        list.add(new CountryModel("tv", "Tuvalu", "+688", "🇹🇻"));
        list.add(new CountryModel("ug", "Uganda", "+256", "🇺🇬"));
        list.add(new CountryModel("ua", "Ukraine", "+380", "🇺🇦"));
        list.add(new CountryModel("ae", "United Arab Emirates", "+971", "🇦🇪"));
        list.add(new CountryModel("gb", "United Kingdom", "+44", "🇬🇧"));
        list.add(new CountryModel("us", "United States", "+1", "🇺🇸"));
        list.add(new CountryModel("uy", "Uruguay", "+598", "🇺🇾"));
        list.add(new CountryModel("vi", "US Virgin Islands", "+1340", "🇻🇮"));
        list.add(new CountryModel("uz", "Uzbekistan", "+998", "🇺🇿"));
        list.add(new CountryModel("vu", "Vanuatu", "+678", "🇻🇺"));
        list.add(new CountryModel("ve", "Venezuela", "+58", "🇻🇪"));
        list.add(new CountryModel("vn", "Vietnam", "+84", "🇻🇳"));
        list.add(new CountryModel("wf", "Wallis And Futuna", "+681", "🇼🇫"));
        list.add(new CountryModel("ye", "Yemen", "+967", "🇾🇪"));
        list.add(new CountryModel("zm", "Zambia", "+260", "🇿🇲"));
        list.add(new CountryModel("zw", "Zimbabwe", "+263", "🇿🇼"));
        list.add(new CountryModel("ax", "Åland Islands", "+358", "🇦🇽"));
        searchList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountryCodeItemBinding binding=CountryCodeItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CountryCodeItemBinding binding;
        public ViewHolder(@NonNull CountryCodeItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(list.get(getAbsoluteAdapterPosition()));
                }
            });
        }

        public void setData(CountryModel model) {
            binding.getRoot().setText(model.getCountryFlag() + "  " + model.getCountryName() + "  (" + model.getPhoneCode() + ")");
        }
    }
}
