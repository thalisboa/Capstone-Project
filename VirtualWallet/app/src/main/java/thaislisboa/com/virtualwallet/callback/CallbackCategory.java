package thaislisboa.com.virtualwallet.callback;

import java.util.List;

import thaislisboa.com.virtualwallet.model.Category;

public interface CallbackCategory {

        void onReturn(List<Category> categoryList);

    }

