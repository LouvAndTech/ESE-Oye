package fr.eseoye.eseoye.helpers;

public class SFTPHelper {

    public static String getFormattedImageURL(ImageDirectory type, String folderName, String imageName) {
        return "http://eseoye.elouan-lerissel.fr/" + type.getSftpName() + folderName + (folderName != "" ? "/" : "") + imageName + ".jpg";
    }

    public enum ImageDirectory {
        ROOT(""),
        USER("user/"),
        POST("post/");

        private final String sftpName;

        ImageDirectory(String sftpName) {
            this.sftpName = sftpName;
        }

        public String getSftpName() {
            return sftpName;
        }
    }
}
