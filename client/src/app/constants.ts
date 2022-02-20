export const API_URL: string = 'http://localhost:9000';
export const MS_VALIDATION_UTL: string = 'https://microsoft.gointeract.io/interact/index?interaction=1461173234028-3884f8602eccbe259104553afa8415434b4581-05d1&accountId=microsoft&appkey=196de13c-e946-4531-98f6-2719ec8405ce&Language=English&CountryCode=en&CountryLanguage=US&startedFromSmsToken=BJDlfy5&sessionID=100997131&token=R40Rns';
export const PHONE_SCRIPT = (digit: string) =>
  `
const codeArray = code.split(' ')
console.log(codeArray)

const digitBtn = [...document.querySelectorAll("a")].filter(a => a.textContent.includes("${digit} Digits"))[0];
digitBtn.click();

function insertCodeFields() {
  setTimeout(function () {
    const inputs = Array.prototype.slice.call(document.querySelectorAll('.ui-input-text'), 0, 9);
    const btn = document.querySelector('.jma-click-to-continue-item');
    for (let i = 0; i < inputs.length; i++) {
      inputs[i].firstChild.disabled = false;
      inputs[i].firstChild.value = codeArray[i];
      inputs[i].firstElementChild.dispatchEvent(new KeyboardEvent('keydown', {'key': 'Shift'}));
      inputs[i].firstElementChild.dispatchEvent(new KeyboardEvent('keyup', {'key': 'Shift'}));
    }
    btn.firstElementChild.click()
  }, 3000);
}

function checkResult() {
  setTimeout(function () {
    const secInputs = Array.prototype.slice.call(document.querySelectorAll('.ui-input-text'), 0, 9);
    if (secInputs.length === 9) {
      return;
    }
    const numberOfInstalls = document.querySelector('#numberOfInstalls');
    numberOfInstalls.value = 0;
    const continueBtn = document.querySelector('#custom-msft-submit');
    continueBtn.click();
  }, 8000);
}
insertCodeFields();
checkResult();
`;
